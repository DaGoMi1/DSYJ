function addOption() {
    var optionsDiv = document.getElementById('options');

    // 새로운 선택지와 삭제 버튼을 담을 새로운 div 생성
    var optionDiv = document.createElement('div');
    optionDiv.classList.add('option-container');

    // 선택지를 위한 input 엘리먼트 생성
    var newOptionInput = document.createElement('input');
    newOptionInput.type = 'text';
    newOptionInput.name = 'options';
    newOptionInput.placeholder = '선택지';

    // 선택지 삭제 버튼 생성
    var removeButton = document.createElement('button');
    removeButton.type = 'button';
    removeButton.innerText = '삭제';

    // 삭제 버튼에 스타일 추가
    removeButton.style.border = '1px solid black';
    removeButton.style.color = 'black';

    removeButton.addEventListener('click', function() {
        // 삭제 버튼을 클릭하면 해당 선택지를 포함한 div와 줄 바꿈을 제거
        optionsDiv.removeChild(optionDiv);
        optionsDiv.removeChild(lineBreak);
    });

    // input과 삭제 버튼을 optionDiv에 추가
    optionDiv.appendChild(newOptionInput);
    optionDiv.appendChild(removeButton);

    // optionDiv를 optionsDiv에 추가
    optionsDiv.appendChild(optionDiv);

    // 각 선택지마다 줄 바꿈 추가
    var lineBreak = document.createElement('br');
    optionsDiv.appendChild(lineBreak);
}