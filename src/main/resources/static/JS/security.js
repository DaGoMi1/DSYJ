function checkUserId() {
    // 아이디 입력값 가져오기
    var userId = document.getElementById("userId").value;

    // 정규표현식을 사용하여 길이, 특수문자, 숫자 포함 여부 체크
    var regex = /^(?=.*[a-zA-Z0-9!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{4,}$/;

    // 체크 결과 메시지를 표시할 요소 가져오기
    var messageElement = document.getElementById("userIdMessage");

    // 정규표현식을 통한 체크
    if (regex.test(userId)) {
        messageElement.innerHTML = "유효한 아이디입니다.";
    } else {
        messageElement.innerHTML = "아이디는 4자 이상, 특수문자 또는 숫자를 포함해야 합니다.";
    }
}

function checkPassword() {
    // 비밀번호 입력값 가져오기
    var password = document.getElementById("password").value;

    // 정규표현식을 사용하여 규칙 체크
    var regex = /^(?=.*[a-zA-Z])(?=.*\d)(?=.*[!@#$%^&*])[A-Za-z\d!@#$%^&*]{8,}$/;

    // 체크 결과 메시지를 표시할 요소 가져오기
    var messageElement = document.getElementById("passwordMessage");

    // 정규표현식을 통한 체크
    if (regex.test(password)) {
        messageElement.innerHTML = "유효한 비밀번호입니다.";
    } else {
        messageElement.innerHTML = "비밀번호는 최소 8자 이상이어야 하며, 특수문자, 숫자, 대소문자를 포함해야 합니다.";
    }
}