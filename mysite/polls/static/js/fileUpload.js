function setup() {
    document.getElementById('my_file').click();

	$('input[type=file]').change(function (e) {
		$('#customfileupload').html($(this).val());
	});
}