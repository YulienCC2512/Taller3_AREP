// GET request
function loadGetMsg() {
    let nameVar = document.getElementById("nameGet").value;
    const xhttp = new XMLHttpRequest();
    xhttp.onload = function() {
        let resp = JSON.parse(this.responseText);
        // tu servidor responde {"task":"Tarea para <nombre>"}
        document.getElementById("getrespmsg").innerHTML = resp.task;
    }
    xhttp.open("GET", "/app/getTask?name=" + encodeURIComponent(nameVar));
    xhttp.send();
}

// POST request
function loadPostMsg() {
    let nameVar = document.getElementById("namePost").value;

    fetch("/app/postTask", {
        method: 'POST',
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify({name: nameVar})
    })
        .then(res => res.json())
        .then(data => {
            document.getElementById("postrespmsg").innerHTML =
                `<p><strong>${data.status}</strong></p>`;
        })
        .catch(err => {
            document.getElementById("postrespmsg").innerHTML =
                `<p style="color:red;">Error: ${err}</p>`;
        });
}

