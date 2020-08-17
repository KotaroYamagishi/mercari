$(function(){

    setTimeout(function(){
        var depth=2;
        ajaxChildSearchFirst(depth);
    }, 100);

    setTimeout(function(){
        var depth=3;
        ajaxGrandChildSearchFirst(depth);
    },200);

     // categoryのcheckboxのvalueを変更した時
     $("#parentCategory").on("change", function () {
        parentCategory = $(this).val();
        depth = 2;
        ajaxChildSearch(parentCategory, depth);
        // 普通に処理前のchildcategoryのvalもってきてる
    });
    // settimeoutメソッドで取得を少し遅らせてる
    $("#parentCategory").on("change", function(){
        setTimeout(function(){
            childCategory = $("#childCategory").val();
            depth = 3;
            ajaxGrandChildSearch(childCategory, depth);
        },200);
    });

    
    $("#resultDiv1").on("change", "#childCategory",function () {
        childCategory = $("#childCategory").val();
        depth = 3;
        ajaxGrandChildSearch(childCategory, depth);
    });
})

// 遷移時に表示するためのメソッド
function ajaxChildSearchFirst(depth) {
    // event.preventDefault();
    $.ajax({
        type: "GET",
        url: "/ajaxe/search-first",
        data: {
            "depth": depth
        },
        dataType: "html"
    }).done(function (data) {
        $("#resultDiv1").html(data);
    }).fail(function (XMLHttpRequest, textStatus, errorThrown) {
        alert("リクエストに失敗" + textStatus + ":\n" + errorThrown)
    })
}
// 遷移時に表示するためのメソッド
function ajaxGrandChildSearchFirst(depth) {
    // event.preventDefault();
    $.ajax({
        type: "GET",
        url: "/ajaxe/search-first",
        data: {
            "depth": depth
        },
        dataType: "html"
    }).done(function (data) {
        $("#resultDiv2").html(data);
    }).fail(function (XMLHttpRequest, textStatus, errorThrown) {
        alert("リクエストに失敗" + textStatus + ":\n" + errorThrown)
    })
}


// grandChildCategory2回目以降につける時のメソッド
function ajaxChildSearch(parentCategory,depth) {
    // event.preventDefault();
    $.ajax({
        type: "GET",
        url: "/ajaxe/search",
        data: {
            "parentCategory": parentCategory,
            "depth": depth
        },
        dataType: "html"
    }).done(function (data) {
        $("#resultDiv1").html(data);
    }).fail(function (XMLHttpRequest, textStatus, errorThrown) {
        alert("リクエストに失敗" + textStatus + ":\n" + errorThrown)
    })
}
// grandChildCategory2回目以降につける時のメソッド
function ajaxGrandChildSearch(parentCategory,depth) {
    // event.preventDefault();
    $.ajax({
        type: "GET",
        url: "/ajaxe/search",
        data: {
            "parentCategory": parentCategory,
            "depth": depth
        },
        dataType: "html"
    }).done(function (data) {
        $("#resultDiv2").html(data);
    }).fail(function (XMLHttpRequest, textStatus, errorThrown) {
        alert("リクエストに失敗" + textStatus + ":\n" + errorThrown)
    })
}