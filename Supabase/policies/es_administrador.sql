CREATE OR REPLACE FUNCTION public.es_administrador()
RETURNS BOOLEAN
LANGUAGE SQL
STABLE
SECURITY DEFINER
SET search_path = ''
AS $$
    SELECT EXISTS (
        SELECT 1
        FROM public.perfiles
        WHERE id = (SELECT auth.uid())
          AND rol = 'administrador'
    );
$$;

REVOKE ALL ON FUNCTION public.es_administrador() FROM PUBLIC, anon;

GRANT EXECUTE ON FUNCTION public.es_administrador() TO authenticated;