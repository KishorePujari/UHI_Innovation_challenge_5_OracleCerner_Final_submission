let speak_btn = document.querySelector("#click-to-speak");
let submit_btn = document.querySelector("#click-to-submit");

speak_btn.addEventListener("click", (e) => {
  console.log("speech code");
  e.preventDefault();
  var speech = true;
  window.SpeechRecognition = window.webkitSpeechRecognition;
  const recognition = new SpeechRecognition();
  recognition.interimResults = true;
  recognition.addEventListener('result', e => {
    const transcript = Array.from(e.results)
      .map(result => result[0])
      .map(result => result.transcript)
    textarea_speak.innerHTML = transcript;
  })
  if (speech == true) {
    recognition.start();
  }
});

function convert(text) {
  var medicine = "";
  var diagnosed = "";
  var myHeaders = new Headers();
  myHeaders.append("X-Api-Key", "intelli-search-csh");
  myHeaders.append("Content-Type", "application/json");

  var textValue = JSON.stringify(text);


  var raw = JSON.stringify({
    "text": textValue
  });

  var requestOptions = {
    method: 'POST',
    headers: myHeaders,
    body: raw,
    redirect: 'follow'
  };
  // Drug - dosage - Duration - frequency

  fetch("https://intelli-search-csh.herokuapp.com/extractmedication", requestOptions)
    .then(response => response.text())
    .then(result => {
      result = JSON.parse(result);
      // result.Status = "Success";
      // result.medications = [];
      // result.medications.push ({"drugs":"Med1", "dosage":"500 mg","duration":"10 days", "frequency":"8 hours"});
      // result.medications.push ({"drugs":"Med2", "dosage":"500 mg","duration":"10 days", "frequency":"8 hours"});
      if (result.Status == "Success") {
        medicine = "<div class=\"row\">";

        result.medications.map(data => {
          medicine = medicine + "<div class=\"col-xl-3 col-sm-12 col-12\"> <div class=\"card\"><div class=\"card-content\"><div class=\"card-body\"><div class=\"media d-flex\"><div class=\"align-self-center\"> <i class=\"fas fas-blue fa-notes-medical fa-large\"> </i> </div><div class=\"media-body text-right\">";
          medicine = medicine + "<h3>" + data.drugs + " " + data.dosage + "</h3> <span>" + data.duration + " " + data.frequency + "</span>";
          medicine = medicine + "</div></div></div></div></div></div>";
        })
        medicine += "</div>"
        document.getElementById("medication_container").innerHTML = medicine;
      } else {
        document.getElementById("medication_container").innerHTML = "Error";
      }
    })
    .catch(error => console.log('error', error));


  fetch("https://intelli-search-csh.herokuapp.com/extractDisease", requestOptions)
    .then(response => response.text())
    .then(result => {
      result = JSON.parse(result)
      if (result.Status == "Success") {

        diagnosed = "<table class=\"table table-bordered table-hover\"><tbody><tr>";
        result.extracted_diseases.map(data => {
          diagnosed = diagnosed + "<td>" + data + "</td>";
        })
        diagnosed = diagnosed + "</tr></tbody></table>";

        document.getElementById("diagnosis_container").innerHTML = diagnosed;
      } else {
        document.getElementById("diagnosis_container").innerHTML = "Error";
      }
    })
    .catch(error => console.log('error', error));
}
submit_btn.addEventListener("click", (e) => {
  e.preventDefault();

  var textValue = document.getElementById("textarea_speak").value;
  if (textValue == "") {
    alert("Please Eneter text");
    return;
  }

  document.getElementById("medication_container").innerHTML = "";
  document.getElementById("diagnosis_container").innerHTML = "";

  let element = document.getElementById("language");
  let language = element.value;

  if (language != "en") {

    var http = new XMLHttpRequest();

    // Unable to host this online 
    // During demo we will use our local machine to show the demo
    var url = 'http://localhost:3000/translate';
    var params = '';
    http.open('POST', url, true);

    //Send the proper header information along with the request
    http.setRequestHeader('Content-type', 'application/json');

    http.onreadystatechange = function () { //Call a function when the state changes.
      if (http.readyState == 4 && http.status == 200) {
        convert(http.responseText);
        // alert(http.responseText);
      }
    }
    var params = {
      "text": textValue,
      "from": language,
      "to": "en"
    };
    http.send(JSON.stringify(params));
  } else {
    convert(textValue);
  }


});