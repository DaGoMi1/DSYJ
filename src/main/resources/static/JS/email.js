let verified = false;

let emailVerificationData;

function sendNumber() {
    $.ajax({
        url: "/email/send",
        type: "post",
        dataType: "text",
        data: {"email": $("#email").val()},
        success: function (data) {
            // 서버에서의 응답에 따른 처리
            if (data.startsWith("Error")) {
                console.error('이메일 전송 실패');
                // 실패 시 사용자에게 알림을 표시하거나 다른 처리를 추가할 수 있음
            } else {
                console.log('이메일 전송 성공');
                emailVerificationData = data; // 전역 변수에 저장

                // 인증 성공 메시지를 감추고, 인증번호 입력 폼을 다시 보이게 함
                $("#verificationForm").css("display", "block");
                $("#verificationSuccessMessage").css("display", "none");
                $("#verificationFailMessage").css("display", "none");
                verified = false;
            }
        },
        error: function (error) {
            console.error('Error:', error);
            // 네트워크 오류 또는 다른 문제로 인한 실패 시 처리
        }
    });
}

function checkNumber() {
    const enteredCode = $("#code").val();

    if (enteredCode === emailVerificationData) {
        console.log('인증번호 확인 성공');
        verified = true;
        // 인증번호를 입력할 수 있는 폼을 감추고, 성공 메시지를 보이게 함
        $("#verificationForm").css("display", "none");
        $("#verificationSuccessMessage").css("display", "block");
        $("#verificationFailMessage").css("display", "none");
    } else {
        console.error('인증번호 확인 실패');
        // 인증 번호가 틀렸을 때의 동작 추가
        $("#verificationSuccessMessage").css("display", "none");
        $("#verificationFailMessage").css("display", "block");
    }
}

// 등록 버튼 클릭 시 이벤트 처리
$("#registerButton").on("click", function (e) {
    if (!verified) {
        e.preventDefault(); // 인증이 완료되지 않았으면 폼 제출을 막음
        alert("이메일 인증을 완료해주세요.");
    }
});