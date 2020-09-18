import 'package:flupref/flupref.dart';

part 'test.g.dart';

@flupref
abstract class _TestPref extends MmkvModel {
  @entry
  Future<double> test = pref(0.0);
}
