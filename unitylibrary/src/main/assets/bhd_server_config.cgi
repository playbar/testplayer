User nobody 
Control {
  Types {
    text/html { .html }
    application/octet-stream { * }
  }
  IndexNames { index.html }
} 
Server {
  Virtual {
    AnyHost
    Control {
      Alias /
      Location /
    }
  }
} 
