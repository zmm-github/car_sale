package com.zmm.car.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zmm.car.vo.ResultMap;
import com.zmm.car.entity.News;
import com.zmm.car.service.INewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author zmm
 * @since 2023-06-01
 */
@RestController
@RequestMapping("/news")
public class NewsController {
    @Autowired
    private INewsService newsService;

    /* 获取所有新闻列表 */
    @GetMapping("/getNewsList")
    public ResultMap<Map<String, Object>> getNewsList(@RequestParam(value = "title", required = false) String title,
                                                      @RequestParam(value = "author", required = false) String author,
                                                      @RequestParam("currentPage") Long currentPage,
                                                      @RequestParam("pageSize") Long pageSize) {
        // 设置查询条件
        LambdaQueryWrapper<News> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasLength(title), News::getTitle, title);
        queryWrapper.like(StringUtils.hasLength(author), News::getAuthor, author);

        // 设置分页
        Page<News> page = new Page<>(currentPage, pageSize);

        // 进行分页模糊查询
        newsService.page(page, queryWrapper);

        // 设置向前端返回的数据
        HashMap<String, Object> data = new HashMap<>();
        data.put("total", page.getTotal());
        data.put("rows", page.getRecords());

        // 返回
        return ResultMap.success(data);
    }

    /* 新增新闻 */
    @PostMapping("/addNews")
    public ResultMap addNews(@RequestBody News news) {
        Date date = new Date();
        news.setCreateTime(date);
        news.setModifyTime(date);
        newsService.save(news);
        return ResultMap.success("新增新闻成功！");
    }

    /* 修改新闻 */
    @PutMapping("/updateNewsById")
    public ResultMap updateNewsById(@RequestBody News news) {
        Date date = new Date();
        news.setCreateTime(date);
        newsService.updateById(news);
        return ResultMap.success("修改新闻成功！");
    }

    /* 删除新闻 */
    @DeleteMapping("/deleteNewsById/{id}")
    public ResultMap deleteNewsById(@PathVariable("id") Integer id) {
        newsService.removeById(id);
        return ResultMap.success("已成功删除编号 “" + id + "” 的新闻信息！");
    }

    /* 根据id查询指定新闻内容 */
    @GetMapping("/getNewsById/{id}")
    public ResultMap getNewsById(@PathVariable("id") Integer id) {
        News news = newsService.getById(id);
        return ResultMap.success(news);
    }

    /* 批量删除新闻 */
    @DeleteMapping("/deleteNewsByIds")
    public ResultMap deleteNewsByIds(@RequestBody List<Integer> ids) {
        newsService.removeBatchByIds(ids);
        return ResultMap.success("已成功删除编号 “" + ids + "” 的新闻信息！");
    }

    /* 获取所有新闻 */
    @GetMapping("/getAllNews")
    public ResultMap<List<News>> getAllNews() {
        List<News> list = newsService.list();
        return ResultMap.success(list);
    }
}
