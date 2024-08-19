package com.holidai.jejuway.di

import com.holidai.jejuway.data.supabase.SupabaseClient
import com.holidai.jejuway.data.supabase.auth.SupabaseAuth
import com.holidai.jejuway.data.supabase.database.SupabaseDatabase
import org.koin.dsl.module

val supabaseModule = module {
    single { SupabaseClient() }
    single { SupabaseAuth(get(), get()) }
    single { SupabaseDatabase(get(), get()) }
}