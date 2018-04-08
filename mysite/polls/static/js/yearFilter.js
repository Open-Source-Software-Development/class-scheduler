function filterYear() {
    location.href="?year=" + document.getElementsByName("yearSelection")[0].options[document.getElementsByName("yearSelection")[0].selectedIndex].value
}
