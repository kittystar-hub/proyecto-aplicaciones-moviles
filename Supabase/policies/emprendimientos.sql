-- CONSULTA PÚBLICA: solo emprendimientos aprobados
CREATE POLICY "emprendimientos_select_aprobados"
ON public.emprendimientos
FOR SELECT
TO anon, authenticated
USING (estado = 'aprobado');


-- EMPRENDEDORAS: consultar su propio emprendimiento
CREATE POLICY "emprendimientos_select_propios"
ON public.emprendimientos
FOR SELECT
TO authenticated
USING (
    EXISTS (
        SELECT 1
        FROM public.emprendedoras
        WHERE public.emprendedoras.id = emprendimientos.emprendedora_id
          AND public.emprendedoras.auth_user_id = (SELECT auth.uid())
    )
);


-- EMPRENDEDORAS: modificar su propio emprendimiento
CREATE POLICY "emprendimientos_update_propios"
ON public.emprendimientos
FOR UPDATE
TO authenticated
USING (
    EXISTS (
        SELECT 1
        FROM public.emprendedoras
        WHERE public.emprendedoras.id = emprendimientos.emprendedora_id
          AND public.emprendedoras.auth_user_id = (SELECT auth.uid())
    )
)
WITH CHECK (
    EXISTS (
        SELECT 1
        FROM public.emprendedoras
        WHERE public.emprendedoras.id = emprendimientos.emprendedora_id
          AND public.emprendedoras.auth_user_id = (SELECT auth.uid())
    )
    AND estado = 'pendiente'
);


-- ADMINISTRADORES: consultar todos
CREATE POLICY "emprendimientos_select_admin"
ON public.emprendimientos
FOR SELECT
TO authenticated
USING (public.es_administrador());


-- ADMINISTRADORES: modificar todos
CREATE POLICY "emprendimientos_update_admin"
ON public.emprendimientos
FOR UPDATE
TO authenticated
USING (public.es_administrador())
WITH CHECK (public.es_administrador());


-- ADMINISTRADORES: eliminar todos
CREATE POLICY "emprendimientos_delete_admin"
ON public.emprendimientos
FOR DELETE
TO authenticated
USING (public.es_administrador());