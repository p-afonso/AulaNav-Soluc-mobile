package com.example.aulanavcontroller

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.aulanavcontroller.ui.theme.AulaNavControllerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AulaNavControllerTheme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "home") {
                    composable("home") { HomeScreen(navController) }

                    // Pergunta Fácil 1 - Atualizada
                    composable("facil1") {
                        PistaScreen(
                            pergunta = "Neymar deve ser convocado pra 2026?",
                            respostaCorreta = "Sim",
                            dicas = listOf("É o maior artilheiro da nossa seleção", "A resposta tem apenas 3 letras"),
                            onProxima = { navController.navigate("facil2") },
                            onVoltar = { navController.popBackStack() },
                            onPular = { navController.navigate("facil2") }
                        )
                    }

                    // Pergunta Fácil 2
                    composable("facil2") {
                        PistaScreen(
                            pergunta = "Em qual país será realizada a final da próxima Copa do Mundo em 2026 (junto com México e Canadá)?",
                            respostaCorreta = "Estados Unidos",
                            dicas = listOf("Sua capital é Washington", "Fica na América do Norte"),
                            onProxima = { navController.navigate("pista1") },
                            onVoltar = { navController.popBackStack() },
                            onPular = { navController.navigate("pista1") }
                        )
                    }

                    composable("pista1") {
                        PistaScreen(
                            pergunta = "Qual país sediou e venceu a primeira Copa do Mundo em 1930?",
                            respostaCorreta = "Uruguai",
                            dicas = listOf("É um vizinho do Brasil", "Sua capital é Montevidéu"),
                            onProxima = { navController.navigate("pista2") },
                            onVoltar = { navController.popBackStack() },
                            onPular = { navController.navigate("pista2") }
                        )
                    }

                    composable("pista2") {
                        PistaScreen(
                            pergunta = "Qual jogador detém o recorde de maior número de gols em uma única edição de Copa (13 gols em 1958)?",
                            respostaCorreta = "Just Fontaine",
                            dicas = listOf("Ele jogava pela seleção da França", "O nome dele começa com 'Just'"),
                            onProxima = { navController.navigate("pista3") },
                            onVoltar = { navController.popBackStack() },
                            onPular = { navController.navigate("pista3") }
                        )
                    }

                    composable("pista3") {
                        PistaScreen(
                            pergunta = "Em que ano o Brasil conquistou o seu quinto título mundial (Pentacampeonato)?",
                            respostaCorreta = "2002",
                            dicas = listOf("Foi no século 21", "A Copa foi na Coreia e no Japão"),
                            onProxima = { navController.navigate("tesouro") },
                            onVoltar = { navController.popBackStack() },
                            onPular = { navController.navigate("tesouro") }
                        )
                    }

                    composable("tesouro") { TreasureScreen(navController) }
                }
            }
        }
    }
}

@Composable
fun HomeScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "🏆 Caça ao Tesouro das Copas 🏆",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            lineHeight = 34.sp
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = { navController.navigate("facil1") },
            modifier = Modifier.height(56.dp)
        ) {
            Text("Iniciar Caça ao Tesouro", fontSize = 18.sp)
        }
    }
}

@Composable
fun PistaScreen(
    pergunta: String,
    respostaCorreta: String,
    dicas: List<String>,
    onProxima: () -> Unit,
    onVoltar: () -> Unit,
    onPular: () -> Unit
) {
    var respostaUsuario by remember { mutableStateOf("") }
    var erro by remember { mutableStateOf(false) }
    var dicasReveladas by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Pista Atual:",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = pergunta,
            fontSize = 22.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 16.dp),
            lineHeight = 28.sp
        )

        // Exibição das Dicas
        if (dicasReveladas > 0) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                for (i in 0 until dicasReveladas) {
                    Text(
                        text = "💡 Dica ${i + 1}: ${dicas[i]}",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }
            }
        }

        if (dicasReveladas < dicas.size) {
            TextButton(onClick = { dicasReveladas++ }) {
                Text("Pedir Dica (${dicas.size - dicasReveladas} restantes)")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = respostaUsuario,
            onValueChange = {
                respostaUsuario = it
                erro = false
            },
            label = { Text("Sua resposta") },
            isError = erro,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        if (erro) {
            Text(
                text = "Incorreto! Use as dicas ou tente novamente.",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Botões de Ação
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onVoltar,
                modifier = Modifier.weight(1f)
            ) {
                Text("Voltar")
            }
            Button(
                onClick = {
                    if (respostaUsuario.trim().equals(respostaCorreta, ignoreCase = true)) {
                        onProxima()
                    } else {
                        erro = true
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Confirmar")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        TextButton(onClick = onPular) {
            Text("Pular esta pergunta ⏭️", color = MaterialTheme.colorScheme.secondary)
        }
    }
}

@Composable
fun TreasureScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "🎊 Parabéns! 🎊",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(24.dp))
        
        // O Tesouro Encontrado
        Text(
            text = "🏆",
            fontSize = 100.sp
        )
        Text(
            text = "A TAÇA DO MUNDO É NOSSA!",
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.secondary,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Você provou ser um expert em Copas do Mundo e encontrou o Tesouro Máximo!",
            fontSize = 18.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(48.dp))
        Button(
            onClick = {
                navController.navigate("home") {
                    popUpTo("home") { inclusive = true }
                }
            },
            modifier = Modifier.height(56.dp)
        ) {
            Text("Recomeçar Caça", fontSize = 18.sp)
        }
    }
}
