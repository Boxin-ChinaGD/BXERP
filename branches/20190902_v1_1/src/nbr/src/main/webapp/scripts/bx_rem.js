var dw = document.documentElement.clientWidth;
document.documentElement.style.fontSize = dw / 17.2+ "px";
window.onresize = function(){
    var dw = document.documentElement.clientWidth;
    document.documentElement.style.fontSize = dw / 17.2 + "px";
}