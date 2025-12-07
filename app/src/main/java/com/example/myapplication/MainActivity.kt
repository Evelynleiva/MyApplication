package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.FirebaseFirestore
import android.app.AlertDialog

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                var pantalla by remember { mutableStateOf("menu") }

                when(pantalla) {
                    "menu" -> MenuPrincipal(
                        onRegistrar = { pantalla = "registrar" },
                        onListar = { pantalla = "listar" }
                    )
                    "registrar" -> RegistrarProducto(onVolver = { pantalla = "menu" })
                    "listar" -> ListarProductos(onVolver = { pantalla = "menu" })
                }
            }
        }
    }
}

@Composable
fun MenuPrincipal(onRegistrar: () -> Unit, onListar: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("MENÚ PRINCIPAL", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(32.dp))

        Button(
            onClick = onRegistrar,
            modifier = Modifier.fillMaxWidth(0.7f).height(56.dp)
        ) {
            Text("Registrar Producto")
        }

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = onListar,
            modifier = Modifier.fillMaxWidth(0.7f).height(56.dp)
        ) {
            Text("Listar Productos")
        }
    }
}

@Composable
fun RegistrarProducto(onVolver: () -> Unit) {
    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()

    var codigo by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }
    var url by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Button(onClick = onVolver) {
            Text("← Volver")
        }

        Spacer(Modifier.height(16.dp))
        Text("REGISTRAR PRODUCTO", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = codigo,
            onValueChange = { codigo = it },
            label = { Text("Código de barra") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre del producto") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = precio,
            onValueChange = { precio = it },
            label = { Text("Precio") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = categoria,
            onValueChange = { categoria = it },
            label = { Text("Nombre Categoría") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = url,
            onValueChange = { url = it },
            label = { Text("URL del producto") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                // Validación: Código obligatorio
                if (codigo.isBlank()) {
                    AlertDialog.Builder(context)
                        .setTitle("Error")
                        .setMessage("El código de barra es obligatorio")
                        .setPositiveButton("OK", null)
                        .show()
                    return@Button
                }

                // Validación: Nombre obligatorio
                if (nombre.isBlank()) {
                    AlertDialog.Builder(context)
                        .setTitle("Error")
                        .setMessage("El nombre del producto es obligatorio")
                        .setPositiveButton("OK", null)
                        .show()
                    return@Button
                }

                // Validación: Precio válido
                val precioNum = precio.toDoubleOrNull()
                if (precioNum == null || precioNum <= 0) {
                    AlertDialog.Builder(context)
                        .setTitle("Error")
                        .setMessage("El precio debe ser un número válido mayor a 0")
                        .setPositiveButton("OK", null)
                        .show()
                    return@Button
                }

                // Validación: Categoría obligatoria
                if (categoria.isBlank()) {
                    AlertDialog.Builder(context)
                        .setTitle("Error")
                        .setMessage("La categoría es obligatoria")
                        .setPositiveButton("OK", null)
                        .show()
                    return@Button
                }

                // Validación: URL obligatoria
                if (url.isBlank()) {
                    AlertDialog.Builder(context)
                        .setTitle("Error")
                        .setMessage("La URL es obligatoria")
                        .setPositiveButton("OK", null)
                        .show()
                    return@Button
                }

                // Verificar código único
                db.collection("productos")
                    .whereEqualTo("codigo_barra", codigo)
                    .get()
                    .addOnSuccessListener { docs ->
                        if (!docs.isEmpty) {
                            AlertDialog.Builder(context)
                                .setTitle("Error")
                                .setMessage("El código de barra ya existe en la base de datos")
                                .setPositiveButton("OK", null)
                                .show()
                        } else {
                            // Guardar producto
                            val prod = hashMapOf(
                                "codigo_barra" to codigo,
                                "nombre" to nombre,
                                "precio" to precioNum,
                                "categoria" to categoria,
                                "url" to url
                            )

                            db.collection("productos").add(prod)
                                .addOnSuccessListener {
                                    AlertDialog.Builder(context)
                                        .setTitle("Éxito")
                                        .setMessage("Producto registrado correctamente")
                                        .setPositiveButton("OK", null)
                                        .show()

                                    // Limpiar campos
                                    codigo = ""
                                    nombre = ""
                                    precio = ""
                                    categoria = ""
                                    url = ""
                                }
                                .addOnFailureListener { e ->
                                    AlertDialog.Builder(context)
                                        .setTitle("Error")
                                        .setMessage("Error al guardar: ${e.message}")
                                        .setPositiveButton("OK", null)
                                        .show()
                                }
                        }
                    }
                    .addOnFailureListener { e ->
                        AlertDialog.Builder(context)
                            .setTitle("Error")
                            .setMessage("Error al verificar código: ${e.message}")
                            .setPositiveButton("OK", null)
                            .show()
                    }
            },
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) {
            Text("GUARDAR PRODUCTO")
        }
    }
}

@Composable
fun ListarProductos(onVolver: () -> Unit) {
    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()
    var productos by remember { mutableStateOf<List<Producto>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        db.collection("productos").get()
            .addOnSuccessListener { docs ->
                productos = docs.map { doc ->
                    Producto(
                        codigo_barra = doc.getString("codigo_barra") ?: "",
                        nombre = doc.getString("nombre") ?: "",
                        precio = doc.getDouble("precio") ?: 0.0,
                        categoria = doc.getString("categoria") ?: "",
                        url = doc.getString("url") ?: ""
                    )
                }
                isLoading = false
            }
            .addOnFailureListener { e ->
                AlertDialog.Builder(context)
                    .setTitle("Error")
                    .setMessage("Error al cargar productos: ${e.message}")
                    .setPositiveButton("OK", null)
                    .show()
                isLoading = false
            }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Button(onClick = onVolver) {
            Text("← Volver")
        }

        Spacer(Modifier.height(16.dp))
        Text("LISTA DE PRODUCTOS", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (productos.isEmpty()) {
            Text("No hay productos registrados", modifier = Modifier.padding(16.dp))
        } else {
            LazyColumn {
                items(productos) { prod ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Código de barra: ${prod.codigo_barra}", style = MaterialTheme.typography.bodyMedium)
                            Text("Nombre: ${prod.nombre}", style = MaterialTheme.typography.titleMedium)
                            Text("Categoría: ${prod.categoria}", style = MaterialTheme.typography.bodyMedium)
                            Text("Precio: $${prod.precio}", style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                }
            }
        }
    }
}