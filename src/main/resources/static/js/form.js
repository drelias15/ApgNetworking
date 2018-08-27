$(document).ready(function(){
    $(".chosen-select").chosen({disable_search_threshold: 10});
})

$('buttonId').on('click', function() {

    var data = 'dmc='+ encodeURIComponent(dmc.value);
    $.ajax({
        url:"read",
        data : data,
        type : "POST",

        success : function(response) {
            alert( response );
            $message._show('success', response);
        },
        error : function(xhr, status, error) {
            alert(xhr.responseText);
        },
        complete: function() {
            window.location = 'inbox.html';
        }

    });
});