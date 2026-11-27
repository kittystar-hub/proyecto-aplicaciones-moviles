package com.example.fundaciontextil.data.repository

import com.example.fundaciontextil.data.model.ClienteInsertRemote
import com.example.fundaciontextil.data.model.ClienteRemote
import com.example.fundaciontextil.data.remote.SupabaseClient
import io.github.jan.supabase.postgrest.from

class ClienteRepositoryRemote {

    private val supabase = SupabaseClient.client

    // Registrar un cliente
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
}