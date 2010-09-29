//Show a file selector dialog
var f = plugins.file.showFileOpenDialog();

if (f != null) {
    browsed_file = f;
    id = f.getName();
    type_ = id.substring(id.lastIndexOf(".") + 1);
}
