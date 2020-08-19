$(function () {

    var parentId=$("#ajaxParent").val();
    var childId = $("#ajaxChild").val();
    var grandChildId=$("#ajaxGrandChild").val();
    ajaxChildSearchFirst(parentId,childId,2);

    ajaxGrandChildSearchFirst(childId,grandChildId,3);

    // categoryのcheckboxのvalueを変更した時
    $("#parentCategory").on("change", function () {
        var parentCategory = $(this).val();
        var depth = 2;
        ajaxChildSearch(parentCategory, depth);
        // 普通に処理前のchildcategoryのvalもってきてる
    });
    // settimeoutメソッドで取得を少し遅らせてる
    $("#parentCategory").on("change", function () {
        setTimeout(function () {
            childCategory = $("#childCategory").val();
            depth = 3;
            ajaxGrandChildSearch(childCategory, depth);
        }, 200);
    });


    $("#resultDiv1").on("change", "#childCategory", function () {
        childCategory = $("#childCategory").val();
        depth = 3;
        ajaxGrandChildSearch(childCategory, depth);
    });
})

// 遷移時に表示するためのメソッド
function ajaxChildSearchFirst(parentId,childId,depth) {
    // event.preventDefault();
    $.ajax({
        type: "GET",
        url: "/ajaxe/search-child-first",
        data: {
            "parentId":parentId,
            "childId":childId,
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
function ajaxGrandChildSearchFirst(childId,grandChildId,depth) {
    // event.preventDefault();
    $.ajax({
        type: "GET",
        url: "/ajaxe/search-grandchild-first",
        data: {
            "parentId": childId,
            "childId":grandChildId,
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
function ajaxChildSearch(parentCategory, depth) {
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
function ajaxGrandChildSearch(parentCategory, depth) {
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