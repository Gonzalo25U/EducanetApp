package com.example.educanetapp.viewmodel
import androidx.lifecycle.ViewModel
import com.example.educanetapp.model.Resource
import com.example.educanetapp.model.ResourceType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ResourceViewModel : ViewModel() {

    private val _resources = MutableStateFlow<List<Resource>>(emptyList())
    val resources: StateFlow<List<Resource>> = _resources

    init {
        loadResources()
    }

    private fun loadResources() {
        _resources.value = listOf(
            Resource(
                1,
                "Matemáticas 1° Medio",
                "Libro digital de matemáticas básico.",
                ResourceType.LIBRO,
                "https://www.curriculumnacional.cl/portal/Educacion-General/Matematica/Matematica-1-medio/145564:Matematica-1-Medio-Santillana-Texto-del-estudiante"
            ),
            Resource(
                2,
                "Cómo mejorar tus hábitos de estudio",
                "Artículo educativo con consejos prácticos.",
                ResourceType.ARTICULO,
                "https://ciencialatina.org/index.php/cienciala/article/view/16996"
            ),
            Resource(
                3,
                "Ciencias Naturales: el agua y su ciclo",
                "Video explicativo sobre el ciclo del agua.",
                ResourceType.VIDEO,
                "https://youtu.be/KbLw6cB52vQ?si=LX9-8Kqe6Vo_pz6-"
            )
        )
    }
}