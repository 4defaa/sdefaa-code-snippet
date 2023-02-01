#include <iostream>
#include "meowParser.h"

void init()
{
    std::cout << "猫咪声音解析器初始化...\n";
}

char *parse(const char *content)
{
    char *meaning;
    if (strcmp(content, "喵喵") == 0)
    {
        meaning = "想要食物，想要关注，想要进入房间，生病或者孤单等.";
    }
    else if (strcmp(content, "呼噜") == 0)
    {
        meaning = "感到高兴和舒适或者受伤和疼痛.";
    }
    else if (strcmp(content, "振颤") == 0)
    {
        meaning = "捕猎时的自然行为反应.";
    }
    else if (strcmp(content, "嚎叫") == 0)
    {
        meaning = "领地警告或者发情求偶等.";
    }
    else if (strcmp(content, "咆哮") == 0)
    {
        meaning = "烦躁，生气或者恐惧等.";
    }
    else if (strcmp(content, "嘶嘶") == 0)
    {
        meaning = "生气或者害怕，准备随时战斗.";
    }
    else
    {
        meaning = "无法解析.";
    }
    return meaning;
}

void destroy()
{
    std::cout << "猫咪声音解析器销毁了...\n";
}