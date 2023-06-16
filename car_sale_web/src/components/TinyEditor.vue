<template>
    <div class="tinymce-editor">
        <editor v-model="myValue" :init="init" :disabled="disabled" @onClick="onClick">
        </editor>
    </div>
</template>
   
<script>
import tinymce from 'tinymce/tinymce'
import Editor from '@tinymce/tinymce-vue'
import 'tinymce/themes/silver/theme'
import 'tinymce/plugins/image'
import 'tinymce/plugins/media'
import 'tinymce/plugins/table'
import 'tinymce/plugins/lists'
import 'tinymce/plugins/contextmenu'
import 'tinymce/plugins/wordcount'
import 'tinymce/plugins/colorpicker'
import 'tinymce/plugins/textcolor'
import "tinymce/plugins/preview";
import "tinymce/plugins/code";
import "tinymce/plugins/link";
import "tinymce/plugins/advlist";
import "tinymce/plugins/codesample";
import "tinymce/plugins/hr";
import "tinymce/plugins/fullscreen";
import "tinymce/plugins/textpattern";
import "tinymce/plugins/searchreplace";
import "tinymce/plugins/autolink";
import "tinymce/plugins/directionality";
import "tinymce/plugins/visualblocks";
import "tinymce/plugins/visualchars";
import "tinymce/plugins/template";
import "tinymce/plugins/charmap";
import "tinymce/plugins/nonbreaking";
import "tinymce/plugins/insertdatetime";
import "tinymce/plugins/imagetools";
import "tinymce/plugins/autosave";
import "tinymce/plugins/autoresize";
export default {
    components: {
        Editor
    },
    props: {
        //传入一个value，使组件支持v-model绑定
        value: {
            type: String,
            default: ''
        },
        disabled: {
            type: Boolean,
            default: false
        },
        plugins: {
            type: [String, Array],
            default:
                'preview searchreplace autolink directionality visualblocks visualchars fullscreen image link media template code codesample table charmap hr nonbreaking insertdatetime advlist lists wordcount imagetools textpattern autosave autoresize'
            // 'lists image media table  wordcount '
            // 'image media table link code table lists wordcount'
        },
        toolbar: {
            type: [String, Array],
            default:
                // 'code undo redo | cut copy paste pastetext | bold italic underline strikethrough | alignleft aligncenter alignright alignjustify | bullist numlist outdent indent | lists | removeformat hr'
                'undo redo |  formatselect | bold italic underline | alignleft aligncenter alignright alignjustify | bullist numlist outdent indent | lists image media table | removeformat hr strikethrough'
        }
    },
    data() {
        return {
            //初始化配置
            init: {
                language_url: '/tinymce/langs/zh_CN.js',
                language: 'zh_CN',
                skin_url: '/tinymce/skins/ui/oxide',
                height: 400,
                min_height: 400,
                max_height: 600,
                object_resizing: false, //禁用表格内联样式拖拽拉伸
                // table_resize_bars: false,//禁用表格单元格拖拽拉伸
                toolbar_mode: 'wrap',
                plugins: this.plugins,
                toolbar: this.toolbar,
                content_style: "p {margin: 5px 0;}", //内容样式
                fontsize_formats: "12px 14px 16px 18px 24px 36px 48px 56px 72px",
                font_formats: "宋体='宋体';仿宋='仿宋';微软雅黑='微软雅黑';楷体='楷体';;隶书='隶书';幼圆='幼圆';Andale Mono=andale mono,times;Arial=arial,helvetica,sans-serif;Arial Black=arial black,avant garde;Book Antiqua=book antiqua,palatino;Comic Sans MS=comic sans ms,sans-serif;Courier New=courier new,courier;Georgia=georgia,palatino;Helvetica=helvetica;Impact=impact,chicago;Symbol=symbol;Tahoma=tahoma,arial,helvetica,sans-serif;Terminal=terminal,monaco;Times New Roman=times new roman,times;Trebuchet MS=trebuchet ms,geneva;Verdana=verdana,geneva;Webdings=webdings;Wingdings=wingdings",
                branding: false,//不显示富文本支持方
                // menubar: false,//禁用菜单栏  如果放开菜单栏，则把这个代码注释
                //此处为图片上传处理函数，这个直接用了base64的图片形式上传图片，
                //如需ajax上传可参考https://www.tiny.cloud/docs/configure/file-image-upload/#images_upload_handler
                images_upload_handler: (blobInfo, success, failure) => {
                    // var file = blobInfo.blob();//转化为易于理解的file对象
                    // const img = URL.createObjectURL(file);
                    const img = 'data:image/jpeg;base64,' + blobInfo.base64()
                    success(img)
                },
                resize: false
            },
            myValue: this.value
        }
    },
    mounted() {
        tinymce.init({})
    },
    methods: {
        //添加相关的事件，可用的事件参照文档=> https://github.com/tinymce/tinymce-vue => All available events
        //需要什么事件可以自己增加
        onClick(e) {
            this.$emit('onClick', e, tinymce)
        },
        //可以添加一些自己的自定义事件，如清空内容
        clear() {
            this.myValue = ''
        }
    },
    watch: {
        value(newValue) {
            this.myValue = newValue
        },
        myValue(newValue) {
            this.$emit('input', newValue)
        }
    }
}
</script>
<style scoped></style>