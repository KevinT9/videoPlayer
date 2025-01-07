function cargarListaVideos() {
    fetch('/api/videos')
        .then(response => response.json())
        .then(data => {
            const videoList = document.getElementById("videoList");

            data.forEach(video => {
                // Buscar ' y reemplazar por /u0027
                let videoId = encodeURIComponent(video);
                videoId = videoId.replace(/'/g, '%27');

                const a = document.createElement('a');
                a.href = "#";
                a.onclick = function () {
                    loadVideo(videoId);
                };
                a.innerText = video;
                videoList.appendChild(a);
            });
        })
        .catch(error => console.error('Error al cargar los videos:', error));
}

// Función para cargar un video
function loadVideo(filename) {

    // stompClient.send("/app/connect", {}, message);
    let sessionId = document.getElementById('sessionId').innerText;
    let nameVideoDecode = decodeURIComponent(filename);

    stompClient.send("/app/connect", {}, JSON.stringify({
        type: "videoLoad",
        sessionId: sessionId,
        username: document.getElementById('username').innerText,
        message: "Cargando video: " + filename,
        video: {
            videoUrl: "/videos/" + filename,
            currentTime: 0,
            isPlaying: false,
            nameVideo: nameVideoDecode
        }
    }));
}
