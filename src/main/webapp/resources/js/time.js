const clock = document.getElementById("interactive_time");

function update_clock() {
    let date = new Date();
    clock.innerHTML = date.toLocaleString("ru-RU", {
        hour12: false
    });
}

update_clock();
setInterval(update_clock, 8*1000);
