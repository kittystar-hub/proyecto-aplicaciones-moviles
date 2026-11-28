-- CLIENTES: permisos administrativos
CREATE POLICY "clientes_select_admin"
ON public.clientes
FOR SELECT
TO authenticated
USING (public.es_administrador());

CREATE POLICY "clientes_update_admin"
ON public.clientes
FOR UPDATE
TO authenticated
USING (public.es_administrador())
WITH CHECK (public.es_administrador());

CREATE POLICY "clientes_delete_admin"
ON public.clientes
FOR DELETE
TO authenticated
USING (public.es_administrador());


-- EMPRENDEDORAS: permisos administrativos
CREATE POLICY "emprendedoras_select_admin"
ON public.emprendedoras
FOR SELECT
TO authenticated
USING (public.es_administrador());

CREATE POLICY "emprendedoras_update_admin"
ON public.emprendedoras
FOR UPDATE
TO authenticated
USING (public.es_administrador())
WITH CHECK (public.es_administrador());

CREATE POLICY "emprendedoras_delete_admin"
ON public.emprendedoras
FOR DELETE
TO authenticated
USING (public.es_administrador());