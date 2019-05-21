var UploadFile = {
    init: function () {
        this.setEvents();
    },
    setEvents: function () {
        var me = this;
        $("#upload").on('click', function () {
            me.ajaxSubmitForm();
        });
        $("#container").delegate(".delete", "click", function () {
            var fileId = $(this).data("file-id");
            me.delete(fileId);
        });
    },
    ajaxSubmitForm: function () {
        var option = {
            url: "/api/save",
            type: "POST",
            dataType: "json",
            headers: {"clientCallMode": "ajax"},
            success: function (data) {
                alert(data.message);
                if (data.success) {
                    var str = "<span>" + data.data + "</span><input data-file-id='{data}'" +
                        " type='button' value='删除' class='delete'/>";
                    $("#container").append(str.format(data));
                }
            },
            error: function (data) {
                console.log(data);
                alert("上传失败！");
            }
        };
        $("#file-form").ajaxSubmit(option);
        return false;
    },
    delete: function (fileId) {
        $.post("/api/delete", {"fileId": fileId}, function (data) {
            alert(data.message);
        });
    }
};
$(function () {
   UploadFile.init();
});