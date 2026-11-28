-- CLIENTES: consultar únicamente el registro propio
CREATE POLICY "clientes_select_propio"
ON public.clientes
FOR SELECT
TO authenticated
USING (
    (SELECT auth.uid()) = auth_user_id
);

-- CLIENTES: modificar únicamente el registro propio
CREATE POLICY "clientes_update_propio"
ON public.clientes
FOR UPDATE
TO authenticated
USING (
    (SELECT auth.uid()) = auth_user_id
)
WITH CHECK (
    (SELECT auth.uid()) = auth_user_id
);


-- EMPRENDEDORAS: consultar únicamente el registro propio
CREATE POLICY "emprendedoras_select_propias"
ON public.emprendedoras
FOR SELECT
TO authenticated
USING (
    (SELECT auth.uid()) = auth_user_id
);

-- EMPRENDEDORAS: modificar únicamente el registro propio
CREATE POLICY "emprendedoras_update_propias"
ON public.emprendedoras
FOR UPDATE
TO authenticated
USING (
    (SELECT auth.uid()) = auth_user_id
)
WITH CHECK (
    (SELECT auth.uid()) = auth_user_id
);