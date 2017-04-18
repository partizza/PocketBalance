$(document).ready(function () {


    $(".submenu > a").click(function (e) {
        e.preventDefault();
        var $li = $(this).parent("li");
        var $ul = $(this).next("ul");

        if ($li.hasClass("open")) {
            $ul.slideUp(350);
            $li.removeClass("open");
        } else {
            $(".nav > li > ul").slideUp(350);
            $(".nav > li").removeClass("open");
            $ul.slideDown(350);
            $li.addClass("open");
        }git
    });


    $.ajax({
        type: 'GET',
        url: '../data/user/details',
        dataType: 'json',
        success: function (data) {

            $('.dropdown-toggle').text(data.name + ' ' + data.surname);
            $('.dropdown-toggle').append('<b class="caret"></b>');

        }
    });

});