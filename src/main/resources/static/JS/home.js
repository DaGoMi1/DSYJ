const $hamburger = document.querySelector('#hamburger');
const $overlay = document.querySelector('.overlay');

$hamburger.addEventListener('click',()=>{
    const $menu = document.querySelector('.menu');
    $menu.classList.toggle('active');
})


const $dropbtn = document.querySelectorAll('.dropbtn');
const $hiddenmenu = document.querySelectorAll('.hiddenmenu');

$dropbtn.forEach((button, index) => {
    const dropdownContents = $hiddenmenu[index];
    button.addEventListener('mouseenter', () => {
        dropdownContents.style.display = 'flex';
        $overlay.classList.add('active');
    });

    button.addEventListener('mouseleave', () => {
        dropdownContents.style.display = 'none';
        $overlay.classList.remove('active');
    });

    dropdownContents.addEventListener('mouseenter', () => {
        dropdownContents.style.display = 'flex';
        $overlay.classList.add('active');
    });

    dropdownContents.addEventListener('mouseleave', () => {
        dropdownContents.style.display = 'none';
        $overlay.classList.remove('active');
    });
})

var btnOpen = document.getElementById("btnOpen");
var btnCheck = document.getElementById("btnCheck");
var btnClose = document.getElementById("btnClose");

// modal 창을 감춤
var closeRtn = function () {
    var modal = document.getElementById("modal");
    modal.style.display = "none";
};

// modal 창을 보여줌
btnOpen.onclick = function () {
    var modal = document.getElementById("modal");
    modal.style.display = "block";
};

btnCheck.onclick = closeRtn;
btnClose.onclick = closeRtn;

const $prev = document.querySelector('#prev');
const $next = document.querySelector('#next');

function editPost() {
    // 게시글 수정 로직을 여기에 구현
    console.log("수정 버튼 클릭");
}

function deletePost() {
    // 게시글 삭제 로직을 여기에 구현
    console.log("삭제 버튼 클릭");
}

// inner태그를 다 가져온다. (사진 개수 리스트)
// 리스트의 총 길이-1 은 마지막 인덱스이다.
// index가 0 이면 prev버튼 사용 못함
// index가 마지막 인덱스이면 next버튼 사용못함
// transform: translate(-100 * index) %;
let index = 0;
// $prev.addEventListener('click',()=>{
//   if(index === 0){
//     return
//   }

// })


