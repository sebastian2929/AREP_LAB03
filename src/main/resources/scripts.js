document.addEventListener('DOMContentLoaded', function () {
    const taskForm = document.getElementById('taskForm');
    const showTasksButton = document.getElementById('showTasksButton');
    const clearTasksButton = document.getElementById('clearTasksButton');
    const showImageButton = document.getElementById('showImageButton');
    const tasksListDiv = document.getElementById('tasksList');
    const resultDiv = document.getElementById('result');
    const imageModal = document.getElementById('imageModal');
    const closeModal = document.getElementById('closeModal');

    let tasks = [];
    let showTasks = false;

    taskForm.addEventListener('submit', function (event) {
        event.preventDefault();
        const taskInput = document.getElementById('task');
        const task = taskInput.value.trim();

        if (task) {
            tasks.push(task);
            taskInput.value = ''; // Limpiar campo de entrada
            showSuccessMessage('Tarea añadida exitosamente');
        } else {
            showErrorMessage('Por favor, ingresa una tarea');
        }
    });

    showTasksButton.addEventListener('click', function () {
        showTasks = !showTasks;
        if (showTasks) {
            showTasksButton.textContent = 'Ocultar Tareas';
            tasksListDiv.style.display = 'block'; // Mostrar la lista de tareas
            updateTasksList();
        } else {
            showTasksButton.textContent = 'Mostrar Tareas';
            tasksListDiv.style.display = 'none'; // Ocultar la lista de tareas
        }
    });

    clearTasksButton.addEventListener('click', function () {
        tasks = [];
        tasksListDiv.innerHTML = '';
        showSuccessMessage('Se borraron todas las tareas');
    });

    showImageButton.addEventListener('click', function () {
        imageModal.style.visibility = 'visible';
        imageModal.style.opacity = '1';
    });

    closeModal.addEventListener('click', function () {
        imageModal.style.visibility = 'hidden';
        imageModal.style.opacity = '0';
    });

    function updateTasksList() {
        tasksListDiv.innerHTML = '';
        tasks.forEach(task => {
            const taskDiv = document.createElement('div');
            taskDiv.textContent = task;
            tasksListDiv.appendChild(taskDiv);
        });
    }

    function showSuccessMessage(message) {
        if (resultDiv) {
            resultDiv.textContent = message;
            resultDiv.style.color = 'green';
            resultDiv.style.display = 'block'; // Hacer visible el mensaje
            setTimeout(() => {
                resultDiv.style.display = 'none'; // Ocultar el mensaje después de 3 segundos
            }, 3000);
        } else {
            console.error('No se pudo encontrar el elemento #result para mostrar el mensaje de éxito.');
        }
    }

    function showErrorMessage(message) {
        if (resultDiv) {
            resultDiv.textContent = message;
            resultDiv.style.color = 'red';
            resultDiv.style.display = 'block'; // Hacer visible el mensaje
            setTimeout(() => {
                resultDiv.style.display = 'none'; // Ocultar el mensaje después de 3 segundos
            }, 3000);
        } else {
            console.error('No se pudo encontrar el elemento #result para mostrar el mensaje de error.');
        }
    }
});
