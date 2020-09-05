function extentCalc(extentInput, lengthInput, heightInput, widthInput, countInput){
    extentInput.val('');
    if (lengthInput.val() != '' && heightInput.val() != '' && widthInput.val() != '' && countInput.val() != ''){
        let res = ((lengthInput.val()/1000) * (heightInput.val()/1000) * (widthInput.val()/1000) * (countInput.val()));
        extentInput.val(res.toFixed(3));
        console.log("result: "+res.toFixed(3));
    } else {
        console.log("fill all");
    }
}


//////////////////////////////////////////////////////////////////////////////
$("#sendForPackageLength").change(function() {
    let extent = $("#sendForPackageModalExtent");
    let length = $("#sendForPackageLength");
    let height = $("#sendForPackageHeight");
    let width =  $("#sendForPackageWidth");
    let count =  $("#sendForPackageModalCount");

    extentCalc(extent, length, height, width, count);
});

$("#sendForPackageHeight").change(function() {
    let extent = $("#sendForPackageModalExtent");
    let length = $("#sendForPackageLength");
    let height = $("#sendForPackageHeight");
    let width =  $("#sendForPackageWidth");
    let count =  $("#sendForPackageModalCount");

    extentCalc(extent, length, height, width, count);
});

$("#sendForPackageWidth").change(function() {
    let extent = $("#sendForPackageModalExtent");
    let length = $("#sendForPackageLength");
    let height = $("#sendForPackageHeight");
    let width =  $("#sendForPackageWidth");
    let count =  $("#sendForPackageModalCount");

    extentCalc(extent, length, height, width, count);
});

$("#sendForPackageModalCount").change(function() {
    let extent = $("#sendForPackageModalExtent");
    let length = $("#sendForPackageLength");
    let height = $("#sendForPackageHeight");
    let width =  $("#sendForPackageWidth");
    let count =  $("#sendForPackageModalCount");

    extentCalc(extent, length, height, width, count);
});
/////////////////////////////////////////////////////////////////////////////

/////////////////////////////////////////////////////////////////////////////
$("#editPackageLength").change(function() {
    let extent = $("#editPackageModalExtent");
    let length = $("#editPackageLength");
    let height = $("#editPackageHeight");
    let width =  $("#editPackageWidth");
    let count =  $("#editPackageModalCount");

    extentCalc(extent, length, height, width, count);
});

$("#editPackageHeight").change(function() {
    let extent = $("#editPackageModalExtent");
    let length = $("#editPackageLength");
    let height = $("#editPackageHeight");
    let width =  $("#editPackageWidth");
    let count =  $("#editPackageModalCount");

    extentCalc(extent, length, height, width, count);
});

$("#editPackageWidth").change(function() {
    let extent = $("#editPackageModalExtent");
    let length = $("#editPackageLength");
    let height = $("#editPackageHeight");
    let width =  $("#editPackageWidth");
    let count =  $("#editPackageModalCount");

    extentCalc(extent, length, height, width, count);
});


$("#editPackageModalCount").change(function() {
    let extent = $("#editPackageModalExtent");
    let length = $("#editPackageLength");
    let height = $("#editPackageHeight");
    let width =  $("#editPackageWidth");
    let count =  $("#editPackageModalCount");

    extentCalc(extent, length, height, width, count);
});
/////////////////////////////////////////////////////////////////////////////
