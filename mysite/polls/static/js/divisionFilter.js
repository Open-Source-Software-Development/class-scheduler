function filterDivision() {
    location.href="?division=" + document.getElementsByName("divisionSelection")[0].options[document.getElementsByName("divisionSelection")[0].selectedIndex].value
}
