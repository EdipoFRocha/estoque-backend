-- >>> LINHAS-CHAVE para isolar no schema <<<
SET search_path TO mvp_clean, public;
CREATE SCHEMA IF NOT EXISTS mvp_clean;

-- === Tabelas base ===
create table if not exists material (
                                        id bigserial primary key,
                                        code varchar(50) not null unique,
                                        description varchar(200) not null
);

create table if not exists warehouse (
                                         id serial primary key,
                                         name varchar(80) not null unique
);

create table if not exists location (
                                        id bigserial primary key,
                                        warehouse_id int not null references warehouse(id),
                                        code varchar(60) not null,
                                        unique (warehouse_id, code)
);

create table if not exists lot (
                                   id bigserial primary key,
                                   material_id bigint not null references material(id),
                                   warehouse_id int not null references warehouse(id),
                                   location_id bigint not null references location(id),
                                   qty_total numeric(18,6) not null,
                                   qty_available numeric(18,6) not null,
                                   created_at timestamptz not null default now(),
                                   nf_number varchar(50),
                                   invoice_item_id bigint,
                                   note text
);

create table if not exists stock_movement (
                                              id bigserial primary key,
                                              ts timestamptz not null default now(),
                                              type varchar(30) not null,
                                              lot_id bigint references lot(id),
                                              material_id bigint not null references material(id),
                                              warehouse_id int not null references warehouse(id),
                                              location_id bigint not null references location(id),
                                              qty numeric(18,6) not null,
                                              note text
);

create or replace view v_stock_on_hand as
select
    material_id,
    warehouse_id,
    location_id,
    sum(qty_available) as qty
from lot
group by material_id, warehouse_id, location_id;

insert into warehouse (name) values ('WH1') on conflict do nothing;
insert into material (code, description) values ('MAT-001','Material teste') on conflict do nothing;

do $$
    declare v_wh1 int;
    begin
        select id into v_wh1 from warehouse where name = 'WH1';
        if not exists (select 1 from location where warehouse_id = v_wh1 and code = 'LOC-1') then
            insert into location (warehouse_id, code) values (v_wh1, 'LOC-1');
        end if;
    end$$;

create or replace function fn_register_receipt(
    p_nf_number text,
    p_invoice_item_id bigint,
    p_material_id bigint,
    p_qty numeric,
    p_warehouse_id int,
    p_location_id bigint,
    p_note text
) returns bigint
    language plpgsql as $$
declare v_lot_id bigint;
begin
    if p_qty is null or p_qty <= 0 then
        raise exception 'Quantidade deve ser > 0';
    end if;

    insert into lot(material_id, warehouse_id, location_id, qty_total, qty_available, nf_number, invoice_item_id, note)
    values (p_material_id, p_warehouse_id, p_location_id, p_qty, p_qty, p_nf_number, p_invoice_item_id, p_note)
    returning id into v_lot_id;

    insert into stock_movement(type, lot_id, material_id, warehouse_id, location_id, qty, note)
    values ('RECEIPT', v_lot_id, p_material_id, p_warehouse_id, p_location_id, p_qty, coalesce(p_note,''));

    return v_lot_id;
end $$;
