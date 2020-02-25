## Mall-Front
### 基于Vue开发的商城前台页面
    
### 所用技术

- Vue 2.x
- Vuex
- Vue Router
- [Element UI](http://element.eleme.io/#/zh-CN)
- ES6
- webpack
- axios
- Node.js

### 本地开发运行
- 启动后端项目后，在 `config/index.js` 中修改你的后端接口地址配置
- 在项目根文件夹下先后执行命令 `npm install` 、 `npm run dev`
- 商城前台端口默认9999 http://localhost:9999
## 如果想部署到nginx
- 先后执行命令 `npm install` 、 `npm run build` 将打包生成的 `dist` 静态文件放置服务器中，若使用Nginx等跨域请配置路由代理
