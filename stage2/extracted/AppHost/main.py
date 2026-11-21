import sys, os
sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
import importlib.util
spec = importlib.util.spec_from_file_location("app", os.path.join(os.path.dirname(__file__), "app.pyd"))
app = importlib.util.module_from_spec(spec)
sys.modules["app"] = app
spec.loader.exec_module(app)
if __name__ == "__main__":
    app.run()
