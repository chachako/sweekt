import 'package:source_gen/source_gen.dart';
import 'package:build/build.dart';

import 'flupref_codegen.dart';

Builder fluprefGenerator(BuilderOptions options) =>
    SharedPartBuilder([FluprefGenerator()], 'flupref_generator');
