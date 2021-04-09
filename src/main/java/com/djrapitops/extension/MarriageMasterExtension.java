/*
    Copyright(c) 2021 AuroraLS3

    The MIT License(MIT)

    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files(the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and / or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions :
    The above copyright notice and this permission notice shall be included in
    all copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
    THE SOFTWARE.
*/
package com.djrapitops.extension;

import at.pcgamingfreaks.MarriageMaster.API.Marriage;
import at.pcgamingfreaks.MarriageMaster.API.MarriageMasterPlugin;
import at.pcgamingfreaks.MarriageMaster.API.MarriagePlayer;
import com.djrapitops.plan.extension.CallEvents;
import com.djrapitops.plan.extension.DataExtension;
import com.djrapitops.plan.extension.annotation.DataBuilderProvider;
import com.djrapitops.plan.extension.annotation.NumberProvider;
import com.djrapitops.plan.extension.builder.ExtensionDataBuilder;
import com.djrapitops.plan.extension.icon.Color;
import com.djrapitops.plan.extension.icon.Family;
import com.djrapitops.plan.extension.icon.Icon;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

/**
 * DataExtension.
 *
 * @author AuroraLS3
 */
public abstract class MarriageMasterExtension implements DataExtension {

    private final MarriageMasterPlugin marriageMaster;

    public MarriageMasterExtension() {
        marriageMaster = getMarriageMaster();
    }

    MarriageMasterExtension(boolean forTest) {
        marriageMaster = null;
    }

    public abstract MarriageMasterPlugin getMarriageMaster();

    @Override
    public CallEvents[] callExtensionMethodsOn() {
        return new CallEvents[]{
                CallEvents.SERVER_EXTENSION_REGISTER,
                CallEvents.PLAYER_JOIN,
                CallEvents.PLAYER_LEAVE
        };
    }

    @NumberProvider(
            text = "Marriages",
            description = "How many marriages are there",
            iconName = "ring", iconColor = Color.AMBER
    )
    public long numberOfMarriages() {
        return marriageMaster.getMarriages().size();
    }

    @DataBuilderProvider
    public ExtensionDataBuilder playerData(UUID playerUUID) {
        MarriagePlayer data = marriageMaster.getPlayerData(playerUUID);

        boolean married = data.isMarried();
        return newExtensionDataBuilder()
                .addValue(Boolean.class, valueBuilder("Is Married")
                        .description("Does the player have an active marriage")
                        .priority(100)
                        .icon(Icon.called("ring").of(Color.AMBER).build())
                        .buildBoolean(married))
                .addValue(String.class, () -> married ?
                        valueBuilder("Married to")
                                .description("Who the player is married to")
                                .showInPlayerTable()
                                .showAsPlayerPageLink()
                                .priority(90)
                                .icon(Icon.called("ring").of(Color.AMBER).build())
                                .buildString(Optional.ofNullable(data.getMarriageData())
                                        .map(m -> m.getPartner(data))
                                        .map(MarriagePlayer::getName)
                                        .orElse(null))
                        : null)
                .addValue(Long.class, () -> married ?
                        valueBuilder("Wedding date")
                                .description("When was the wedding held")
                                .formatAsDateWithYear()
                                .priority(80)
                                .icon(Icon.called("calendar").of(Family.REGULAR).of(Color.AMBER).build())
                                .buildNumber(Optional.ofNullable(data.getMarriageData())
                                        .map(Marriage::getWeddingDate)
                                        .map(Date::getTime)
                                        .orElse(0L))
                        : null)
                .addValue(String.class, () -> married ?
                        valueBuilder("Surname")
                                .priority(70)
                                .icon(Icon.called("signature").of(Color.AMBER).build())
                                .buildString(Optional.ofNullable(data.getMarriageData())
                                        .map(Marriage::getSurname)
                                        .orElse(null))
                        : null)
                .addValue(String.class, () -> married ?
                        valueBuilder("Wed by")
                                .showAsPlayerPageLink()
                                .priority(60)
                                .icon(Icon.called("user-tie").of(Color.AMBER).build())
                                .buildString(Optional.ofNullable(data.getMarriageData())
                                        .map(Marriage::getPriest)
                                        .map(MarriagePlayer::getName)
                                        .orElse(null))
                        : null)
                .addValue(String.class, () -> married ?
                        valueBuilder("Home")
                                .priority(50)
                                .icon(Icon.called("map-pin").of(Color.AMBER).build())
                                .buildString(Optional.ofNullable(data.getMarriageData())
                                        .filter(Marriage::isHomeSet)
                                        .map(Marriage::getHome)
                                        .map(home -> home.getWorldName() + " - x:" + home.getX() + " z:" + home.getZ())
                                        .orElse(null))
                        : null)
                .addValue(Boolean.class, valueBuilder("Shares backpack")
                        .description("Does the married couple share their inventory")
                        .priority(10)
                        .icon(Icon.called("shopping-bag").of(Color.BROWN).build())
                        .buildBoolean(married));
    }
}