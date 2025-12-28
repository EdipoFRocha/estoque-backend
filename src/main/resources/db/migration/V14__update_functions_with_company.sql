CREATE OR REPLACE FUNCTION mvp_clean.fn_register_receipt(
    p_nf_number       text,
    p_invoice_item_id bigint,
    p_material_id     bigint,
    p_qty             numeric,
    p_warehouse_id    integer,
    p_location_id     bigint,
    p_note            text,
    p_company_id      bigint
) RETURNS bigint
LANGUAGE plpgsql
AS $$
DECLARE
    v_movement_id bigint;
BEGIN

    INSERT INTO mvp_clean.stock_movement (
        type,
        lot_id,
        material_id,
        warehouse_id,
        location_id,
        qty,
        note,
        reason,
        created_at,
        company_id
    ) VALUES (
        'RECEIPT',
        NULL,
        p_material_id,
        p_warehouse_id,
        p_location_id,
        p_qty,
        p_note,
        'RECEIPT',
        now(),
        p_company_id
    )
    RETURNING id INTO v_movement_id;

    RETURN v_movement_id;
END;
$$;

CREATE OR REPLACE FUNCTION mvp_clean.fn_register_sale_fifo(
    p_material_id   bigint,
    p_qty           numeric,
    p_warehouse_id  bigint,
    p_location_id   bigint,
    p_note          text,
    p_company_id    bigint
) RETURNS bigint
LANGUAGE plpgsql
AS $$
DECLARE
    v_movement_id bigint;
BEGIN

    INSERT INTO mvp_clean.stock_movement (
        type,
        lot_id,
        material_id,
        warehouse_id,
        location_id,
        qty,
        note,
        reason,
        created_at,
        company_id
    ) VALUES (
        'SALE',
        NULL,
        p_material_id,
        p_warehouse_id,
        p_location_id,
        p_qty * -1,
        p_note,
        'SALE',
        now(),
        p_company_id
    )
    RETURNING id INTO v_movement_id;

    RETURN v_movement_id;
END;
$$;

CREATE OR REPLACE FUNCTION mvp_clean.fn_register_return(
    p_material_id   bigint,
    p_qty           numeric,
    p_warehouse_id  bigint,
    p_location_id   bigint,
    p_note          text,
    p_company_id    bigint
) RETURNS bigint
LANGUAGE plpgsql
AS $$
DECLARE
    v_movement_id bigint;
BEGIN

    INSERT INTO mvp_clean.stock_movement (
        type,
        lot_id,
        material_id,
        warehouse_id,
        location_id,
        qty,
        note,
        reason,
        created_at,
        company_id
    ) VALUES (
        'RETURN',
        NULL,
        p_material_id,
        p_warehouse_id,
        p_location_id,
        p_qty,
        p_note,
        'RETURN',
        now(),
        p_company_id
    )
    RETURNING id INTO v_movement_id;

    RETURN v_movement_id;
END;
$$;

CREATE OR REPLACE FUNCTION mvp_clean.fn_register_transfer(
    p_material_id     bigint,
    p_qty             numeric,
    p_warehouse_id    bigint,
    p_from_location   bigint,
    p_to_location     bigint,
    p_note            text,
    p_company_id      bigint
) RETURNS bigint
LANGUAGE plpgsql
AS $$
DECLARE
    v_out_id bigint;
    v_in_id  bigint;
BEGIN
    INSERT INTO mvp_clean.stock_movement (
        type,
        lot_id,
        material_id,
        warehouse_id,
        location_id,
        qty,
        note,
        reason,
        created_at,
        company_id
    ) VALUES (
        'TRANSFER_OUT',
        NULL,
        p_material_id,
        p_warehouse_id,
        p_from_location,
        p_qty * -1,
        p_note,
        'TRANSFER',
        now(),
        p_company_id
    )
    RETURNING id INTO v_out_id;

    INSERT INTO mvp_clean.stock_movement (
        type,
        lot_id,
        material_id,
        warehouse_id,
        location_id,
        qty,
        note,
        reason,
        created_at,
        company_id
    ) VALUES (
        'TRANSFER_IN',
        NULL,
        p_material_id,
        p_warehouse_id,
        p_to_location,
        p_qty,
        p_note,
        'TRANSFER',
        now(),
        p_company_id
    )
    RETURNING id INTO v_in_id;

    RETURN v_out_id;
END;
$$;
