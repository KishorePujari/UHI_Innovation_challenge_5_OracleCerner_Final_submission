let speak_btn = document.querySelector("#click-to-speak1");
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


submit_btn.addEventListener("click", (e) => {


  e.preventDefault();
  var medicine = "";
  var diagnosed = "";
  var textValue = document.getElementById("textarea_speak").value
  var textValue = JSON.stringify(textValue)
  var myHeaders = new Headers();
  myHeaders.append("X-Api-Key", "intelli-search-csh");
  myHeaders.append("Content-Type", "application/json");

  var raw = JSON.stringify({
    "text": textValue
  });

  var requestOptions = {
    method: 'POST',
    headers: myHeaders,
    body: raw,
    redirect: 'follow'
  };

  fetch("https://intelli-search-csh.herokuapp.com/extractmedication", requestOptions)
    .then(response => response.text())
    .then(result => {
      result = JSON.parse(result)
      result.medications.map(data => {
        medicine = medicine + data.drugs + " " + data.dosage + " " + data.duration + " " + data.frequency
        medicine += "<br/>"
      })
      document.getElementById("medicines").innerHTML = medicine;
    })
    .catch(error => console.log('error', error));


  fetch("https://intelli-search-csh.herokuapp.com/extractDisease", requestOptions)
    .then(response => response.text())
    .then(result => {
      result = JSON.parse(result)
      result.extracted_diseases.map(data => {
        if(diagnosed == ""){
          diagnosed = data 
        }
        diagnosed = diagnosed + "," + data

      })
      document.getElementById("diagnosis").innerHTML = diagnosed;
    })
    .catch(error => console.log('error', error));


});