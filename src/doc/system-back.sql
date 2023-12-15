
insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('直播用户', '3', '1', '/system/user', 'C', '0', 'system:user:view', '#', 'admin', sysdate(), '', null, '直播用户菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('直播用户查询', @parentId, '1',  '#',  'F', '0', 'system:user:list',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('直播用户新增', @parentId, '2',  '#',  'F', '0', 'system:user:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('直播用户修改', @parentId, '3',  '#',  'F', '0', 'system:user:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('直播用户删除', @parentId, '4',  '#',  'F', '0', 'system:user:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('直播用户导出', @parentId, '5',  '#',  'F', '0', 'system:user:export',       '#', 'admin', sysdate(), '', null, '');




insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('审核直播', '3', '1', '/system/live', 'C', '0', 'system:live:view', '#', 'admin', sysdate(), '', null, '审核直播菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('审核直播查询', @parentId, '1',  '#',  'F', '0', 'system:live:list',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('审核直播新增', @parentId, '2',  '#',  'F', '0', 'system:live:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('审核直播修改', @parentId, '3',  '#',  'F', '0', 'system:live:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('审核直播删除', @parentId, '4',  '#',  'F', '0', 'system:live:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('审核直播导出', @parentId, '5',  '#',  'F', '0', 'system:live:export',       '#', 'admin', sysdate(), '', null, '');




insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('热门搜索', '3', '1', '/system/search', 'C', '0', 'system:search:view', '#', 'admin', sysdate(), '', null, '热门搜索菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('热门搜索查询', @parentId, '1',  '#',  'F', '0', 'system:search:list',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('热门搜索新增', @parentId, '2',  '#',  'F', '0', 'system:search:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('热门搜索修改', @parentId, '3',  '#',  'F', '0', 'system:search:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('热门搜索删除', @parentId, '4',  '#',  'F', '0', 'system:search:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('热门搜索导出', @parentId, '5',  '#',  'F', '0', 'system:search:export',       '#', 'admin', sysdate(), '', null, '');


insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('热门直播', '3', '1', '/system/detail', 'C', '0', 'system:detail:view', '#', 'admin', sysdate(), '', null, '热门直播菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('热门直播查询', @parentId, '1',  '#',  'F', '0', 'system:detail:list',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('热门直播新增', @parentId, '2',  '#',  'F', '0', 'system:detail:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('热门直播修改', @parentId, '3',  '#',  'F', '0', 'system:detail:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('热门直播删除', @parentId, '4',  '#',  'F', '0', 'system:detail:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('热门直播导出', @parentId, '5',  '#',  'F', '0', 'system:detail:export',       '#', 'admin', sysdate(), '', null, '');
