-- CONSULTA PÚBLICA: fotografías de emprendimientos aprobados
CREATE POLICY "fotos_select_aprobadas"
ON public.fotos_productos
FOR SELECT
TO anon, authenticated
USING (
    EXISTS (
        SELECT 1
        FROM public.emprendimientos
        WHERE public.emprendimientos.id = fotos_productos.emprendimiento_id
          AND public.emprendimientos.estado = 'aprobado'
    )
);


-- EMPRENDEDORAS: consultar fotografías propias
CREATE POLICY "fotos_select_propias"
ON public.fotos_productos
FOR SELECT
TO authenticated
USING (
    EXISTS (
        SELECT 1
        FROM public.emprendimientos e
        INNER JOIN public.emprendedoras em
            ON em.id = e.emprendedora_id
        WHERE e.id = fotos_productos.emprendimiento_id
          AND em.auth_user_id = (SELECT auth.uid())
    )
);


-- EMPRENDEDORAS: agregar fotografías propias
CREATE POLICY "fotos_insert_propias"
ON public.fotos_productos
FOR INSERT
TO authenticated
WITH CHECK (
    EXISTS (
        SELECT 1
        FROM public.emprendimientos e
        INNER JOIN public.emprendedoras em
            ON em.id = e.emprendedora_id
        WHERE e.id = fotos_productos.emprendimiento_id
          AND em.auth_user_id = (SELECT auth.uid())
    )
);


-- EMPRENDEDORAS: eliminar fotografías propias
CREATE POLICY "fotos_delete_propias"
ON public.fotos_productos
FOR DELETE
TO authenticated
USING (
    EXISTS (
        SELECT 1
        FROM public.emprendimientos e
        INNER JOIN public.emprendedoras em
            ON em.id = e.emprendedora_id
        WHERE e.id = fotos_productos.emprendimiento_id
          AND em.auth_user_id = (SELECT auth.uid())
    )
);


-- ADMINISTRADORES: consultar todas las fotografías
CREATE POLICY "fotos_select_admin"
ON public.fotos_productos
FOR SELECT
TO authenticated
USING (public.es_administrador());


-- ADMINISTRADORES: agregar fotografías
CREATE POLICY "fotos_insert_admin"
ON public.fotos_productos
FOR INSERT
TO authenticated
WITH CHECK (public.es_administrador());


-- ADMINISTRADORES: eliminar fotografías
CREATE POLICY "fotos_delete_admin"
ON public.fotos_productos
FOR DELETE
TO authenticated
USING (public.es_administrador());