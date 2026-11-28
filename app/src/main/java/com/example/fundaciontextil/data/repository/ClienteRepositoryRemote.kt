package com.example.fundaciontextil.data.repository

import com.example.fundaciontextil.data.model.ClienteInsertRemote
import com.example.fundaciontextil.data.model.ClienteRemote
import com.example.fundaciontextil.data.remote.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.auth.auth

class ClienteRepositoryRemote {

    private val supabase = SupabaseClient.client

    // Registrar cliente
    suspend fun registrarCliente(cliente: ClienteInsertRemote) {
        supabase
            .from("clientes")
            .insert(cliente)
    }

    // Obtener todos los clientes
    suspend fun obtenerClientes(): List<ClienteRemote> {
        return supabase
            .from("clientes")
            .select()
            .decodeList<ClienteRemote>()
    }

    // Obtener cliente por ID
    suspend fun obtenerClientePorId(id: String): ClienteRemote? {
        return supabase
            .from("clientes")
            .select {
                filter {
                    eq("id", id)
                }
            }
            .decodeList<ClienteRemote>()
            .firstOrNull()
    }

    // Obtener los datos del cliente autenticado
    suspend fun obtenerClienteActual(): ClienteRemote? {
        val usuarioId = supabase.auth.currentUserOrNull()?.id
            ?: return null

        return supabase
            .from("clientes")
            .select {
                filter {
                    eq("auth_user_id", usuarioId)
                }
            }
            .decodeList<ClienteRemote>()
            .firstOrNull()
    }

    // Actualizar cliente
    suspend fun actualizarCliente(
        id: String,
        cliente: ClienteInsertRemote
    ) {
        supabase
            .from("clientes")
            .update(cliente) {
                filter {
                    eq("id", id)
                }
            }
    }

    // Eliminar cliente
    suspend fun eliminarCliente(id: String) {
        supabase
            .from("clientes")
            .delete {
                filter {
                    eq("id", id)
                }
            }
    }
}