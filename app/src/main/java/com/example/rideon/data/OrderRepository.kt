package com.example.rideon.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.example.rideon.model.Order
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "order_history")
class OrderRepository(private val context : Context){
    private val gson = Gson()
    companion object{
        private val ORDERS_KEY = stringPreferencesKey("orders_list")
    }
    val ordersFlow: Flow<List<Order>> = context.dataStore.data.map { preferences ->
        val jsonString = preferences[ORDERS_KEY] ?: "[]"
        val type = object : TypeToken<List<Order>>() {}.type
        gson.fromJson(jsonString, type)
    }
    suspend fun addOrder(order: Order) {
        context.dataStore.edit { preferences ->
            val currentJson = preferences[ORDERS_KEY] ?: "[]"
            val type = object : TypeToken<List<Order>>() {}.type
            val currentOrders: MutableList<Order> = gson.fromJson(currentJson, type)
            currentOrders.add(order)
            preferences[ORDERS_KEY] = gson.toJson(currentOrders)
        }
    }
}