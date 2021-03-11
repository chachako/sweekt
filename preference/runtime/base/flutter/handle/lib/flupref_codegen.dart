/*
 * Copyright (c) 2021. Rin Orz (凛)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 *
 * Github home page: https://github.com/RinOrz
 */

import 'package:flupref/annotations.dart';
import 'package:source_gen/source_gen.dart';
import 'package:analyzer/dart/element/element.dart';
import 'package:build/build.dart';

class FluprefGenerator extends GeneratorForAnnotation<MakeFlupref> {
  @override
  generateForAnnotatedElement(
    Element element,
    ConstantReader annotation,
    BuildStep buildStep,
  ) {
    final List<String> delegationFields = [];
    if (element is ClassElement &&
        element.name[0] == '_' &&
        element.isAbstract) {
      element.fields.forEach((field) {
        element.metadata.forEach((annotation) {
          if (annotation.toString().startsWith('@MakeFlupref')) {
            // 变量被注解，为其创建 pref 代理
            delegationFields.add(genFieldDelegation(field));
          }
        });
      });
      final className = element.name.substring(1);
      final dart = element.enclosingElement.toString().split('/').last;
      // 创建类
      return '''
        /// [$className] 所在 dart 文件必须存在以下 part 声明：
        /// part '${dart.replaceAll('.dart', '')}.g.dart';
        
        final $className ${className[0].toLowerCase() + className.substring(1)} = $className();
        class $className extends _$className {
          ${delegationFields.join("\n")}
          
          @override
          String get name => '${encodeName(className.replaceFirst('_', '').replaceFirst('Pref', ''))}';
        }
      ''';
    }
    return null;
  }

  genFieldDelegation(FieldElement field) {
    final name = field.name;
    final type = field.type;
    final typeStr = type.toString();
    // remove Future< & >
    final subType = typeStr.substring(7, typeStr.length - 1);
    return '''
      $type get $name async =>
        decode<$subType>('${encodeName(name)}', defaultValue: await super.$name);
      set $name($type value) =>
        encode<$subType>('${encodeName(name)}', value);
      /// return the native platform invoked result
      Future<bool> set${name[0].toUpperCase() + name.substring(1)}($subType value) async => 
        await encode<$subType>('${encodeName(name)}', pref<$subType>(value));
    ''';
  }

  encodeName(String str) {
    final length = str.length;
    var result = "";
    for (var i = 0; i < length; i++) {
      result = result + encodeChar(str[i]);
    }
    return result;
  }

  encodeChar(String character) {
    switch (character.toLowerCase()) {
      case 'a':
        return 'ִ';
      case 'b':
        return 'ׁ';
      case 'c':
        return 'ׅ';
      case 'd':
        return 'ܼ';
      case 'e':
        return '࡛';
      case 'f':
        return 'ٖ';
      case 'g':
        return '݈';
      case 'h':
        return '˙';
      case 'i':
        return '໋';
      case 'j':
        return '֒';
      case 'k':
        return '݁';
      case 'l':
        return 'ؚ';
      case 'm':
        return '՝';
      case 'n':
        return '՛';
      case 'o':
        return '՟';
      case 'p':
        return 'ܿ';
      case 'q':
        return 'ּ';
      case 'r':
        return 'វ';
      case 's':
        return '٬';
      case 't':
        return '݇';
      case 'u':
        return '༹';
      case 'v':
        return '་';
      case 'w':
        return 'ܳ';
      case 'x':
        return '݅';
      case 'y':
        return 'ࠫ';
      case 'z':
        return 'ࠣ';
      default:
        return character;
    }
  }
}
