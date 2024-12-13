const canvas = document.getElementById("graph");

const width = canvas.width;
const height = canvas.height;
const ctx = canvas.getContext("2d");

const hiddenInputX = $('#formParameters\\:hiddenX').last()[0];

function runGrapher() {

    const FIGURE_COLOR = "#567efb99";

    function drawGraph() {
        ctx.font = "13px sans-serif";
        ctx.fillStyle = "#FFF";
        ctx.fillRect(0, 0, width, height);

        ctx.fillStyle = FIGURE_COLOR;
        // 1st quadrant rectangle
        ctx.fillRect(width / 2, height / 3, width / 3, height / 6);

        // 2nd quadrant sector
        ctx.beginPath();
        ctx.arc(width / 2, height / 2, width / 6, Math.PI, Math.PI*3/2, false);
        ctx.lineTo(width / 2, height / 2);
        ctx.fill();

        // 4th quadrant rectangle
        ctx.beginPath();
        ctx.moveTo(width/2, 5*height/7.5);
        ctx.lineTo(2*4*width/9.6, height/2);
        ctx.lineTo(width/2, height/2);
        ctx.fill();

        ctx.beginPath();
        ctx.moveTo(0, height/2);
        ctx.lineTo(width, height/2);
        ctx.lineTo(width-10, height/2-10);
        ctx.moveTo(width, height/2);
        ctx.lineTo(width-10,height/2+10);
        ctx.stroke();

        ctx.beginPath();
        ctx.moveTo(width/2, height);
        ctx.lineTo(width/2, 0);
        ctx.lineTo(width/2-10, 10);
        ctx.moveTo(width/2, 0);
        ctx.lineTo(width/2+10, 10);
        ctx.stroke();


        ctx.fillStyle = "#000";
        const labels = ["-R", "-R/2", "0", "R/2", "R"];

        for (let i = 1; i < 6; i++) {
            ctx.beginPath();
            ctx.moveTo((i * width) / 6, height / 2 - 5);
            ctx.lineTo((i * width) / 6, height / 2 + 5);
            ctx.moveTo(width / 2 - 5, (i * height) / 6);
            ctx.lineTo(width / 2 + 5, (i * height) / 6);
            ctx.stroke();

            ctx.textAlign = "center";
            ctx.textBaseline = "bottom";
            ctx.fillText(labels[i - 1], (i * width) / 6, height / 2 - 7);

            ctx.textAlign = "left";
            ctx.textBaseline = "middle";
            ctx.fillText(labels[i - 1], width / 2 + 7, height - (i * height) / 6);
        }

        const attempts = getAttempts();

        let maxR = -1

        $('#formParameters\\:rs input').each(function(index) {
            const input = this;
            if (input.checked) {
                const r = input.value;
                if(r > maxR){
                    maxR = r;
                }
            }
        });

        const chartLabel = document.getElementsByClassName('chart-label')[0];
        const rText = maxR == -1 ? 'not set' : maxR;
        chartLabel.innerText = 'R = ' + rText;

        if(maxR > -1){

            attempts.forEach((attempt) => {

                const k = attempt.r / maxR;
                const x = attempt.x / maxR * k * width / 3 + width / 2;
                const y = -attempt.y / maxR * k * height / 3 + height / 2;


                ctx.fillStyle = (attempt.hit ? '#11ff11' : '#ff3333');
                if(attempt.r != maxR){
                    ctx.fillStyle = '#777';
                }
                ctx.beginPath();
                ctx.arc(x, y, 5, 0, Math.PI * 2);
                ctx.fill();
            });
        }
    }

    return {
        drawGraph,
    };
}

runGrapher().drawGraph();

function getAttempts() {
    const attempts = [];
    $('#maintable tbody tr').each(function() {
        const tr = this;
        if (tr.cells.length < 6) {
            return;
        }

        if (tr.cells[0].innerText === '' || tr.cells[0].innerText === null || tr.cells[0].innerText === undefined) {
            return;
        }
        hiddenInputX.value = 0;

        const x = Number(tr.cells[0].innerText);
        const y = Number(tr.cells[1].innerText);
        const r = Number(tr.cells[2].innerText);
        const hit = tr.cells[5].innerText == "true";
        attempts.push({x,y,r,hit});
    })
    return attempts;
}

$('#formParameters\\:submit').on( "click", () => {
    setTimeout(() => {
        runGrapher().drawGraph();
    }, 400)
})

$('#formParameters\\:rs input').on( "click", () => {
    runGrapher().drawGraph();
})
canvas.onclick = (e) => {
    let rsSelectedNumber = 0;
    let r = null;
    $('#formParameters\\:rs input').each(function(index) {
        const input = this;
        if (input.checked) {
            rsSelectedNumber++
            r = input.value;
        }
    });

    if (rsSelectedNumber != 1) {
        alert("Please select the only one value for R first");
        return;
    }

    const xClicked =
        Math.round(((2 * e.offsetX) / width - 1) * r * 1.5 * 100) / 100;
    const yClicked =
        Math.round(((-2 * e.offsetY) / height + 1) * r * 1.5 * 100) / 100;

    if (yClicked) {
        document.getElementById('formParameters:submit').disable = false;
    }

    const yInput = document.getElementById('formParameters:y');
    yInput.value = yClicked;

    hiddenInputX.value = xClicked;

    validateYClick()

    $('#formParameters\\:submit')[0].click()

    setTimeout(() => {
        hiddenInputX.value = 0;
        runGrapher().drawGraph();
    }, 500)
};


