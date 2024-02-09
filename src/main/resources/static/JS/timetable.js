const $main = document.querySelector('main');
const $addScheduleBtn = document.querySelectorAll('.addSchedule');
const $addScheduleModal = document.querySelector('.addScheduleModal');
const $completeAddSchedule = document.querySelector('#completeAddSchedule');
const $start_time = document.querySelector('#start_time');
const $end_time = document.querySelector('#end_time');
const $cancelAddSchedule = document.querySelector('#cancelAddSchedule');


const days = ['월','화','수','목','금'];
const getDay = (index) => {
    return days[index];
}

const makeTimetable = (grade) => {
    const timetable = document.querySelector(`#timetable-${grade}`)
    for(let i=9; i<18; i++){
        const hour = `${i}:00 ~ ${i+1}:00`
        const row = document.createElement('tr');
        const timecell = document.createElement('th');
        timecell.textContent=hour;
        row.appendChild(timecell);
        for(let j=0; j<5; j++){
            const cell = document.createElement('td');
            // 요일-학년-시간
            cell.id = `${getDay(j)}-${grade}-${i}`;
            row.appendChild(cell);
        }
        timetable.appendChild(row);
    }
}

makeTimetable(1);
makeTimetable(2);
makeTimetable(3);
makeTimetable(4);

const openAddScheduleModal = () => {
    $overlay.classList.add('strongActive');
    $addScheduleModal.style.display = 'inline-block';
}
const closeAddScheduleModal = () => {
    $overlay.classList.remove('strongActive');
    $addScheduleModal.style.display = 'none';
}

let selectedGrade = '';
$addScheduleBtn.forEach((button,index)=>{
    button.addEventListener('click', ()=>{
        selectedGrade = index+1;
        console.log(selectedGrade);
        openAddScheduleModal();
    });
})
$completeAddSchedule.addEventListener('click',()=>{
    addSchedule(selectedGrade);
    postApi().then(data => {
        // 성공적으로 데이터를 전송한 경우 수행할 작업
        console.log('Data saved successfully:', data);
    }).catch(error => {
        // 데이터 전송 중에 오류가 발생한 경우 수행할 작업
        console.error('Error saving data:', error);
    });
})

const schedule = [];

const addSchedule = (grade) => {
    let lectureName = document.querySelector('#lecture_name').value;
    let days = '';
    let isChecked = false;
    document.getElementsByName('radio').forEach((radio)=>{
        if(radio.checked === true){
            days = radio.value;
            isChecked = true;
        }
    })

    let startTime = +$start_time.options[$start_time.selectedIndex].textContent;
    let endTime = +$end_time.options[$end_time.selectedIndex].textContent;
    let selectedColor = document.querySelector('#select_color').value;
    if(lectureName === ''){
        alert('강의명을 입력하세요');
        return
    }
    if(!isChecked){
        alert('요일을 선택하세요');
        return
    }
    if(startTime >= endTime){
        alert('종료시간이 시작시간보다 앞섭니다.');
        return
    }
    if(!addSubject(days,lectureName,startTime,endTime,selectedColor)){
        return
    }
    console.log(schedule);
    renderSchedule();
    closeAddScheduleModal();
}

const addSubject = (days,lectureName,startTime,endTime,selectedColor) => {
    if(duplicateCheck(days,startTime,endTime,selectedGrade)){
        const subject = {'grade': selectedGrade, 'days' : days, 'lectureName' : lectureName, 'startTime' : startTime, 'endTime' : endTime, 'color':selectedColor}
        schedule.push(subject)
        return true
    }
    else{
        alert('해당 시간에 이미 수업이 있습니다.');
        return false
    }
}

const duplicateCheck = (days,startTime,endTime,selectedGrade) => {
    let isDuplicateCheck = true;
    schedule.forEach((v,i)=>{
        const existingDay = v.days;
        const existingStartTime = v.startTime;
        const existingEndTime = v.endTime;
        const existingGrade = v.grade;
        if( (v.days === days) && (v.grade === selectedGrade) &&
            ((startTime >= existingStartTime && startTime < existingEndTime )||
                (endTime > existingStartTime && endTime <= existingEndTime) ||
                (startTime <= existingStartTime && endTime >= existingEndTime)) ) { // 같은날 일 때
            isDuplicateCheck = false;
            return isDuplicateCheck
        }
    })
    return isDuplicateCheck
}

const renderSchedule = () => {
    schedule.forEach((v,i)=>{
        const cellMerge = document.querySelector(`#${v.days}-${v.grade}-${v.startTime}`);
        cellMerge.setAttribute('rowspan',(v.endTime-v.startTime));
        cellMerge.innerHTML = `
    <div class = "btns">
    <button onclick="editSchedule('${v.days}',${v.grade},${v.startTime},${v.endTime})"><i class="fa-solid fa-pen"></i></button>
    <button onclick="deleteSchedule('${v.days}',${v.grade},${v.startTime},${v.endTime})"><i class="fa-solid fa-trash"></i></button>
    </div>
    <div>${v.lectureName}</div>
    `
        cellMerge.style.backgroundColor = v.color;
        console.log('111');
        //숨기기
        for(let j=v.startTime+1; j<v.endTime; j++){
            const hiddenCell = document.querySelector(`#${v.days}-${v.grade}-${j}`);
            hiddenCell.style.display = 'none';
        }
    })
}

//편집을 누르고 취소를 누르면 다시 되돌려야 함
let isClickCancel = false;
$cancelAddSchedule.addEventListener('click',()=>{
    closeAddScheduleModal();
})

const editSchedule = (days,grade,startTime,endTime) => {
    openAddScheduleModal();
    if(isClickCancel === true){
        isClickCancel = false;
        return
    }
    deleteSchedule(days,grade,startTime,endTime);
}

const deleteSchedule = (days,grade,startTime,endTime) => {
    schedule.forEach((v,i)=>{
        if((v.days === days) && (v.grade === grade) && (v.startTime == startTime) && (v.endTime === endTime) ){
            schedule.splice(i,1);
            console.log(schedule);
        }
    })
    const cancelMergeCell = document.querySelector(`#${days}-${grade}-${startTime}`);
    cancelMergeCell.setAttribute('rowspan','');
    cancelMergeCell.style.backgroundColor = 'white';
    cancelMergeCell.textContent = '';
    for(let i=startTime+1; i<endTime; i++){
        document.querySelector(`#${days}-${grade}-${i}`).style.display = 'table-cell';
    }
}

const url = "/timeTable/save-schedule"
const postApi = async () => {
    try {
        const requestBody = {
            schedule: schedule || null,
        };

        const response = await fetch(url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(requestBody),
        });

        if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
        }

        const data = await response.json();
        console.log('Data saved successfully:', data);
    } catch (error) {
        console.error('Error saving data:', error);
    }
};