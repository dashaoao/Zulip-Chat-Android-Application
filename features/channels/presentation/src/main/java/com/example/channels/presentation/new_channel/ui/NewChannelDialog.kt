package com.example.channels.presentation.new_channel.ui

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.channels.presentation.new_channel.NewChannelViewModel
import com.example.channels.presentation.new_channel.di.NewChannelComponentViewModel
import com.example.channels.presentation.new_channel.model.NewChannelNavEvent
import com.example.common.ui.daggerViewModel
import com.example.common.ui.theme.ChatTheme

@Composable
fun NewChannelDialog(
    onDismissRequest: () -> Unit,
) {
    val componentViewModel = viewModel<NewChannelComponentViewModel>()
    val viewModel = daggerViewModel {
        componentViewModel.newChannelComponent.getViewModel()
    }

    NewChannelDialogLayout(
        onDismissRequest = onDismissRequest,
        viewModel = viewModel,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewChannelDialogLayout(
    onDismissRequest: () -> Unit,
    viewModel: NewChannelViewModel,
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.navEvent.collect {
            when (it) {
                NewChannelNavEvent.Exit -> onDismissRequest()
            }
        }
    }

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.error.collect { errorMessage ->
            Toast.makeText(context, errorMessage.stringValue(context), Toast.LENGTH_SHORT).show()
        }
    }

    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            shape = MaterialTheme.shapes.large,
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Create Channel",
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = 8.dp),
                    fontSize = 24.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Normal
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Name:",
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onSecondary,
                        fontWeight = FontWeight.Light
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    TextField(
                        value = state.text,
                        onValueChange = {
                            viewModel.onChannelNameChanged(it)
                        },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth(),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = MaterialTheme.colorScheme.secondary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                            cursorColor = MaterialTheme.colorScheme.secondary,
                            containerColor = Color.Transparent
                        ),
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    NewChannelTextButton(
                        onDismissRequest,
                        "Cancel",
                        MaterialTheme.colorScheme.surfaceTint
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    NewChannelTextButton(
                        { viewModel.addNewChannel() },
                        "Create",
                        MaterialTheme.colorScheme.secondary
                    )
                }
            }
        }
    }
}

@Composable
fun NewChannelTextButton(action: () -> Unit, btnText: String, btnColor: Color) {
    TextButton(
        onClick = {
            action()
        },
        shape = MaterialTheme.shapes.medium,
        colors = ButtonDefaults.textButtonColors(
            containerColor = btnColor
        )
    ) {
        Text(
            text = btnText,
            color = MaterialTheme.colorScheme.onSecondary,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
        )
    }
}

@Preview
@Composable
private fun NewChannelDialogPreview() {
    ChatTheme(
        darkTheme = true
    ) {
        NewChannelDialog(
            {}
        )
    }
}
