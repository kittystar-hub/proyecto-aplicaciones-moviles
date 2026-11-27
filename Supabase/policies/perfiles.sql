CREATE POLICY "perfiles_select_propio"
ON public.perfiles
FOR SELECT
TO authenticated
USING (
    (SELECT auth.uid()) = id
);