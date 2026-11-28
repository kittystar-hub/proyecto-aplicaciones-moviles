-- ==========================================
-- LECTURA DE IMÁGENES
-- ==========================================

-- Cada usuario puede consultar su propia foto de perfil.
CREATE POLICY "storage_perfiles_select_propio"
ON storage.objects
FOR SELECT
TO authenticated
USING (
    bucket_id = 'imagenes-fundacion'
    AND (storage.foldername(name))[1] = 'perfiles'
    AND (storage.foldername(name))[2] = (SELECT auth.uid())::text
);

-- Cualquier visitante puede consultar imágenes de emprendimientos aprobados.
CREATE POLICY "storage_emprendimientos_select_aprobados"
ON storage.objects
FOR SELECT
TO anon, authenticated
USING (
    bucket_id = 'imagenes-fundacion'
    AND (storage.foldername(name))[1] = 'emprendimientos'
    AND EXISTS (
        SELECT 1
        FROM public.emprendimientos e
        WHERE e.id::text = (storage.foldername(name))[2]
          AND e.estado = 'aprobado'
    )
);

-- Las emprendedoras pueden consultar las imágenes de su emprendimiento.
CREATE POLICY "storage_emprendimientos_select_propio"
ON storage.objects
FOR SELECT
TO authenticated
USING (
    bucket_id = 'imagenes-fundacion'
    AND (storage.foldername(name))[1] = 'emprendimientos'
    AND EXISTS (
        SELECT 1
        FROM public.emprendimientos e
        INNER JOIN public.emprendedoras em
            ON em.id = e.emprendedora_id
        WHERE e.id::text = (storage.foldername(name))[2]
          AND em.auth_user_id = (SELECT auth.uid())
    )
);

-- Los administradores pueden consultar todas las imágenes.
CREATE POLICY "storage_admin_select_todos"
ON storage.objects
FOR SELECT
TO authenticated
USING (
    bucket_id = 'imagenes-fundacion'
    AND public.es_administrador()
);


-- ==========================================
-- CARGA DE IMÁGENES
-- ==========================================

-- Cada usuario puede subir su propia foto de perfil.
CREATE POLICY "storage_perfiles_insert_propio"
ON storage.objects
FOR INSERT
TO authenticated
WITH CHECK (
    bucket_id = 'imagenes-fundacion'
    AND (storage.foldername(name))[1] = 'perfiles'
    AND (storage.foldername(name))[2] = (SELECT auth.uid())::text
);

-- Las emprendedoras pueden subir imágenes a su emprendimiento.
CREATE POLICY "storage_emprendimientos_insert_propio"
ON storage.objects
FOR INSERT
TO authenticated
WITH CHECK (
    bucket_id = 'imagenes-fundacion'
    AND (storage.foldername(name))[1] = 'emprendimientos'
    AND EXISTS (
        SELECT 1
        FROM public.emprendimientos e
        INNER JOIN public.emprendedoras em
            ON em.id = e.emprendedora_id
        WHERE e.id::text = (storage.foldername(name))[2]
          AND em.auth_user_id = (SELECT auth.uid())
    )
);

-- Los administradores pueden subir imágenes.
CREATE POLICY "storage_admin_insert"
ON storage.objects
FOR INSERT
TO authenticated
WITH CHECK (
    bucket_id = 'imagenes-fundacion'
    AND public.es_administrador()
);


-- ==========================================
-- ELIMINACIÓN DE IMÁGENES
-- ==========================================

-- Cada usuario puede eliminar su propia foto de perfil.
CREATE POLICY "storage_perfiles_delete_propio"
ON storage.objects
FOR DELETE
TO authenticated
USING (
    bucket_id = 'imagenes-fundacion'
    AND (storage.foldername(name))[1] = 'perfiles'
    AND (storage.foldername(name))[2] = (SELECT auth.uid())::text
);

-- Las emprendedoras pueden eliminar imágenes de su emprendimiento.
CREATE POLICY "storage_emprendimientos_delete_propio"
ON storage.objects
FOR DELETE
TO authenticated
USING (
    bucket_id = 'imagenes-fundacion'
    AND (storage.foldername(name))[1] = 'emprendimientos'
    AND EXISTS (
        SELECT 1
        FROM public.emprendimientos e
        INNER JOIN public.emprendedoras em
            ON em.id = e.emprendedora_id
        WHERE e.id::text = (storage.foldername(name))[2]
          AND em.auth_user_id = (SELECT auth.uid())
    )
);

-- Los administradores pueden eliminar cualquier imagen.
CREATE POLICY "storage_admin_delete"
ON storage.objects
FOR DELETE
TO authenticated
USING (
    bucket_id = 'imagenes-fundacion'
    AND public.es_administrador()
);