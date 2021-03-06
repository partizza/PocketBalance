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
        }
    });

    var path = window.location.pathname;
    if(path == '/setting/account' || path == '/setting/transaction'){
        var $li = $('.submenu');
        var $ul = $('.submenu').children('ul');

        $(".nav > li > ul").slideUp(350);
        $(".nav > li").removeClass("open");
        $ul.slideDown(350);
        $li.addClass("open");
    }

    $.ajax({
        type: 'GET',
        url: '../data/user/details',
        dataType: 'json',
        success: function (data) {

            $('#user-info').text(data.name + ' ' + data.surname);
            $('#user-info').append('<b class="caret"></b>');
            sessionStorage.setItem("bookId", data.bookId );

        },
        error: function () {
            sessionStorage.setItem("bookId", -1 );
        }
    });

});