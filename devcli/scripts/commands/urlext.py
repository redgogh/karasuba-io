"""

    Copyright (C) 2019-2024 RedGogh All rights reserved.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

"""
import console
import sys
from urllib.parse import urlparse, parse_qs

pathname = __file__.replace('\\', '/')
script_name = pathname.split('/')[-1].split('.')[0]

configure = {
    'desc': 'URL扩展工具',
    'sys': 'Windows/Linux/MacOS',
}

def reg(subparsers):
    """
    注册命令的处理函数。

    该函数接收一个子解析器作为参数，用于在命令行工具中注册子命令及其相关选项。
    通常在此函数中会定义具体的子命令及其参数，便于后续的命令解析和处理。

    :param subparsers: argparse 模块创建的子解析器对象，用于添加子命令。
    """
    parser = subparsers.add_parser(script_name, help=f"{configure['desc']} ({configure['sys']})")
    parser.add_argument('url', type=str, help='URL')
    parser.add_argument('-qs', action='store_true', help='解析URL参数')


def handle(args):
    """
    处理命令行参数并执行相应操作。

    该函数接收解析后的命令行参数作为参数，根据参数内容执行特定的业务逻辑。
    通常用于在命令被调用后，根据用户输入的参数执行相应的功能。

    :param args: argparse 模块解析后的参数对象，包含用户输入的参数及选项。
    """
    if args.qs:
        console.write('-qs:', color=None)
        url = urlparse(args.url)
        query_params = parse_qs(url.query)
        for param, value in query_params.items():
            console.write(f'  {console.RED}{param}{console.END}: {console.GREEN}{value[0]}{console.END}', color=None)