let stompClient = null;

function connect() {
  stompClient = Stomp.over(new SockJS("/hw-websocket"));
  stompClient.connect(
    {},
    (frame) => {
      console.log("Connected: " + frame);
      sendConnect();
      stompClient.subscribe("/topic/users", (response) => {
        addUsersToHTMLTable(JSON.parse(response.body));
      });
    },
    (error) => {
      $("#noConnection").show();
    }
  );
}

const addOnClickListenerForSave = () => {
  $("#btnSave").click(function () {
    const user = {
      name: $("#saveForm-name").val(),
      login: $("#saveForm-login").val(),
      password: $("#saveForm-pass").val(),
    };
    stompClient.send("/app/saveUser", {}, JSON.stringify(user));
    $("#saveForm-name").val("");
    $("#saveForm-login").val("");
    $("#saveForm-pass").val("");
    $("#modalLoginForm").modal("hide");
  });
};

const sendConnect = () => {
  stompClient.send("/app/connect");
};

const addUsersToHTMLTable = (users) => {
  $.each(users, function (index, user) {
    $("#userList tr:last").after(
      "<tr>" +
        "<td>" +
        user.name +
        "</td>" +
        "<td>" +
        user.login +
        "</td>" +
        "<td>" +
        user.password +
        "</td>" +
        "</tr>"
    );
  });
};

const processDocumentReady = () => {
  $("#noConnection").hide();
  connect();
  addOnClickListenerForSave();
};

$(document).ready(processDocumentReady);
