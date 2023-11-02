package com.joeun.midproject.service;

import java.util.List;


import com.joeun.midproject.dto.TeamApp;

public interface TeamAppService {
  
  public int insert(TeamApp teamApp);
  
  public List<TeamApp> listByLeader(TeamApp teamApp);

  public List<TeamApp> listByMember(TeamApp teamApp);

  public int delete(TeamApp teamApp);

  public TeamApp read(TeamApp teamApp);

  public int accept(TeamApp teamApp);

  public int denied(TeamApp teamApp);

  public int confirmed(TeamApp teamApp);

}
