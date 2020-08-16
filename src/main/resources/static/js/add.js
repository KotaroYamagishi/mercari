$(function(){

    setTimeout(function(){
        var parentCategory=$("#parentCategory").val();
        var depth=2;
        ajaxEditParent(parentCategory, depth);
    }, 100);

    setTimeout(function(){
        var childCategory=$("#childCategory").val();
        var depth=3;
        ajaxEditChild(childCategory, depth);
    },200);

     // categoryのcheckboxのvalueを変更した時
     $("#parentCategory").on("change", function () {
        parentCategory = $(this).val();
        depth = 2;
        ajaxEditParent(parentCategory, depth);
        // 普通に処理前のchildcategoryのvalもってきてる
    });
    // settimeoutメソッドで取得を少し遅らせてる
    $("#parentCategory").on("change", function(){
        setTimeout(function(){
            childCategory = $("#childCategory").val();
            depth = 3;
            ajaxEditChild(childCategory, depth);
        },200);
    });

    
    $("#resultDiv1").on("change", "#childCategory",function () {
        childCategory = $("#childCategory").val();
        depth = 3;
        ajaxEditChild(childCategory, depth);
    });
})

// childcategory2回目以降に変更する時のメソッド
function ajaxEditParent(parentCategory, depth) {
    // event.preventDefault();
    $.ajax({
        type: "GET",
        url: "/ajaxe/edit-child",
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
function ajaxEditChild(childCategory,depth) {
    // event.preventDefault();
    $.ajax({
        type: "GET",
        url: "/ajaxe/edit-grandchild",
        data: {
            "childCategory": childCategory,
            "depth": depth
        },
        dataType: "html"
    }).done(function (data) {
        $("#resultDiv2").html(data);
    }).fail(function (XMLHttpRequest, textStatus, errorThrown) {
        alert("リクエストに失敗" + textStatus + ":\n" + errorThrown)
    })
}